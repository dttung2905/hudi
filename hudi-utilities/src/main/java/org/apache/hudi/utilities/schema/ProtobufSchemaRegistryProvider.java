package org.apache.hudi.utilities.schema;


import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.exception.HoodieIOException;
import org.apache.hudi.util.StreamerUtil;
import org.apache.spark.api.java.JavaSparkContext;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

import java.io.IOException;
import java.util.Collections;

public class ProtobufSchemaRegistryProvider extends ProtobufSchemaProvider {

    private TypedProperties config;
    private SchemaRegistryClient schemaRegistry;
    public ProtobufSchemaRegistryProvider(TypedProperties props, JavaSparkContext jssc) {
        super(props, jssc);
    }

    public ProtobufSchema fetchSchemaFromRegistry(String registryUrl) throws IOException, RestClientException {
        String subjectName = getSubjectName(registryUrl);
        final SchemaMetadata metadata = schemaRegistry.getLatestSchemaMetadata(subjectName);
        final int id = metadata.getId();

        final ProtobufSchema schemaFromRegistry = ((ProtobufSchema) schemaRegistry.getSchemaById(id));
        return schemaFromRegistry;

    }
    protected String getSubjectName(String registryUrl) {
        final String[] registryUrlArr = registryUrl.split("/");
        return registryUrlArr[registryUrlArr.length-3];

    }

    public void SchemaRegistryProvider(TypedProperties props) {
        this.config = props;
        StreamerUtil.checkRequiredProperties(props, Collections.singletonList(SchemaRegistryProvider.Config.SRC_SCHEMA_REGISTRY_URL_PROP));
    }

    private ProtobufSchema getSchema(String registryUrl) throws IOException, RestClientException {

        return fetchSchemaFromRegistry(registryUrl);
    }

    @Override
    public ProtobufSchema getSourceSchema() {
        String registryUrl = config.getString(SchemaRegistryProvider.Config.SRC_SCHEMA_REGISTRY_URL_PROP);
        try {
            return getSchema(registryUrl);
        } catch (IOException ioe) {
            throw new HoodieIOException("Error reading source schema from registry :" + registryUrl, ioe);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProtobufSchema getTargetSchema() {
        String registryUrl = config.getString(SchemaRegistryProvider.Config.SRC_SCHEMA_REGISTRY_URL_PROP);
        String targetRegistryUrl = config.getString(SchemaRegistryProvider.Config.TARGET_SCHEMA_REGISTRY_URL_PROP, registryUrl);
        try {
            return getSchema(targetRegistryUrl);
        } catch (IOException ioe) {
            throw new HoodieIOException("Error reading target schema from registry :" + registryUrl, ioe);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}

