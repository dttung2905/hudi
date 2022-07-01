package org.apache.hudi.utilities.schema;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import org.apache.hudi.ApiMaturityLevel;
import org.apache.hudi.PublicAPIClass;
import org.apache.hudi.PublicAPIMethod;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

@PublicAPIClass(maturity = ApiMaturityLevel.STABLE)
public abstract class ProtobufSchemaProvider implements Serializable {
    protected TypedProperties config ;
    protected JavaSparkContext jssc;

    public ProtobufSchemaProvider(TypedProperties config) {
        this(config, null);
    }
    protected ProtobufSchemaProvider(TypedProperties config, JavaSparkContext jssc) {
        this.config = config;
        this.jssc = jssc;
    }
    @PublicAPIMethod(maturity = ApiMaturityLevel.STABLE)
    public abstract ProtobufSchema getSourceSchema();

    @PublicAPIMethod(maturity = ApiMaturityLevel.STABLE)
    public ProtobufSchema getTargetSchema() {
        return getSourceSchema();
    }

}
