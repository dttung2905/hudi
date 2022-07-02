/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hudi.utilities.schema;

import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import org.apache.hudi.ApiMaturityLevel;
import org.apache.hudi.PublicAPIClass;
import org.apache.hudi.PublicAPIMethod;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

@PublicAPIClass(maturity = ApiMaturityLevel.STABLE)
public abstract class ProtobufSchemaProvider implements Serializable {
  protected TypedProperties config;
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
