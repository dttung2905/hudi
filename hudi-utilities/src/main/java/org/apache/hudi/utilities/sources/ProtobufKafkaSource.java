package org.apache.hudi.utilities.sources;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.protobuf.ProtobufDatumReader;
import org.apache.avro.protobuf.ProtobufDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.common.util.Option;
import org.apache.hudi.exception.HoodieException;
import org.apache.hudi.exception.HoodieIOException;
import org.apache.hudi.utilities.deltastreamer.HoodieDeltaStreamerMetrics;
import org.apache.hudi.utilities.deser.KafkaAvroSchemaDeserializer;
import org.apache.hudi.utilities.schema.ProtobufSchemaProvider;
import org.apache.hudi.utilities.sources.helpers.KafkaOffsetGen;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.apache.hudi.utilities.exception.HoodieSourceTimeoutException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtobufKafkaSource extends ProtobufSource {
    private static final String NATIVE_KAFKA_KEY_DESERIALIZER_PROP = "key.deserializer";
    private static final String NATIVE_KAFKA_VALUE_DESERIALIZER_PROP = "value.deserializer";
    // These are settings used to pass things to KafkaAvroDeserializer
    public static final String KAFKA_AVRO_VALUE_DESERIALIZER_PROPERTY_PREFIX = "hoodie.deltastreamer.source.kafka.value.deserializer.";
    public static final String KAFKA_AVRO_VALUE_DESERIALIZER_SCHEMA = KAFKA_AVRO_VALUE_DESERIALIZER_PROPERTY_PREFIX + "schema";

    private final KafkaOffsetGen offsetGen;
    private HoodieDeltaStreamerMetrics metrics;
    private final ProtobufSchemaProvider schemaProvider;
    private final String deserializerClassName;
    private static final Logger LOG = LogManager.getLogger(ProtobufKafkaSource.class);

    public ProtobufKafkaSource(TypedProperties props, JavaSparkContext sparkContext, SparkSession sparkSession, ProtobufSchemaProvider schemaProvider, SourceType sourceType) {
        super(props, sparkContext, sparkSession, schemaProvider, sourceType);

        props.put(NATIVE_KAFKA_KEY_DESERIALIZER_PROP, StringDeserializer.class.getName());
        deserializerClassName = props.getString(DataSourceWriteOptions.KAFKA_AVRO_VALUE_DESERIALIZER_CLASS().key(),
                DataSourceWriteOptions.KAFKA_AVRO_VALUE_DESERIALIZER_CLASS().defaultValue());

        try {
            props.put(NATIVE_KAFKA_VALUE_DESERIALIZER_PROP, Class.forName(deserializerClassName).getName());
            if (deserializerClassName.equals(KafkaAvroSchemaDeserializer.class.getName())) {
                if (schemaProvider == null) {
                    throw new HoodieIOException("SchemaProvider has to be set to use KafkaAvroSchemaDeserializer");
                }
                props.put(KAFKA_AVRO_VALUE_DESERIALIZER_SCHEMA, schemaProvider.getSourceSchema().toString());
            }
        } catch (ClassNotFoundException e) {
            String error = "Could not load custom avro kafka deserializer: " + deserializerClassName;
            LOG.error(error);
            throw new HoodieException(error, e);
        }

        this.schemaProvider = schemaProvider;
        this.metrics = metrics;
        offsetGen = new KafkaOffsetGen(props);
    }
    @Override
    protected InputBatch<JavaRDD<GenericRecord>> fetchNewData(Option<String> lastCkptStr, long sourceLimit) {
        try {
            OffsetRange[] offsetRanges = offsetGen.getNextOffsetRanges(lastCkptStr, sourceLimit, metrics);
            long totalNewMsgs = KafkaOffsetGen.CheckpointUtils.totalNewMessages(offsetRanges);
            LOG.info("About to read " + totalNewMsgs + " from Kafka for topic :" + offsetGen.getTopicName());
            if (totalNewMsgs <= 0) {
                return new InputBatch<>(Option.empty(), KafkaOffsetGen.CheckpointUtils.offsetsToStr(offsetRanges));
            }
            JavaRDD<GenericRecord> newDataRDD = toRDD(offsetRanges);
            return new InputBatch<>(Option.of(newDataRDD), KafkaOffsetGen.CheckpointUtils.offsetsToStr(offsetRanges));
        } catch (org.apache.kafka.common.errors.TimeoutException e) {
            throw new HoodieSourceTimeoutException("Kafka Source timed out " + e.getMessage());
        }
    }
    private JavaRDD<GenericRecord> toRDD(OffsetRange[] offsetRanges) {
        if (deserializerClassName.equals(ByteArrayDeserializer.class.getName())) {
            if (schemaProvider == null) {
                throw new HoodieException("Please provide a valid schema provider class when use ByteArrayDeserializer!");
            }
            final ProtobufSchema protobufSchema = schemaProvider.getSourceSchema();
            return KafkaUtils.<String, byte[]>createRDD(sparkContext, offsetGen.getKafkaParams(), offsetRanges,
                    LocationStrategies.PreferConsistent()).map(obj -> converter(obj.value(), protobufSchema) );
        } else {
            return KafkaUtils.createRDD(sparkContext, offsetGen.getKafkaParams(), offsetRanges,
                    LocationStrategies.PreferConsistent()).map(obj -> (GenericRecord) obj.value());
        }
    }
    private GenericRecord converter(byte[] bytes, ProtobufSchema schema) throws IOException {
        DynamicMessage dynamicMessage = DynamicMessage.parseFrom(schema.toDescriptor(), bytes);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ProtobufDatumWriter<DynamicMessage> datumWriter = new ProtobufDatumWriter<>();
        Encoder e = EncoderFactory.get().binaryEncoder(os, null);
        datumWriter.write(dynamicMessage, e);
        e.flush();
        GenericDatumReader<GenericRecord> genericDatumReader = new GenericDatumReader<>();
        return genericDatumReader.read(null,DecoderFactory.get().binaryDecoder(new ByteArrayInputStream(os.toByteArray()),null));

    }
}
