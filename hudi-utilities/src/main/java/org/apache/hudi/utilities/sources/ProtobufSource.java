package org.apache.hudi.utilities.sources;

import org.apache.avro.generic.GenericRecord;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.hudi.utilities.schema.ProtobufSchemaProvider;

public abstract class ProtobufSource extends Source<JavaRDD<GenericRecord>> {

    public ProtobufSource(TypedProperties props, JavaSparkContext sparkContext, SparkSession sparkSession, ProtobufSchemaProvider schemaProvider, SourceType sourceType) {
        super(props, sparkContext, sparkSession, schemaProvider, SourceType.PROTOBUF);
    }
}
