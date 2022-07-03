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

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.exception.HoodieIOException;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;

public class ProtobufSchemaRegistryProvider extends ProtobufSchemaProvider {

  private TypedProperties config;
  private SchemaRegistryClient schemaRegistry;

  public static class Config {
    public static final int CACHED_CAPACITY_PER_SUBJECT = 100;
  }

  public ProtobufSchemaRegistryProvider(TypedProperties props, JavaSparkContext jssc) {
    super(props, jssc);
  }

  public ProtobufSchema fetchSchemaFromRegistry(String registryUrl) throws IOException, RestClientException {
    String subjectName = getSubjectName(registryUrl);
    if (schemaRegistry == null) {
      schemaRegistry = new CachedSchemaRegistryClient(
              registryUrl,
              Config.CACHED_CAPACITY_PER_SUBJECT
      );
    }
    final SchemaMetadata metadata = schemaRegistry.getLatestSchemaMetadata(subjectName);
    final int id = metadata.getId();

    final ProtobufSchema schemaFromRegistry = ((ProtobufSchema) schemaRegistry.getSchemaById(id));
    return schemaFromRegistry;

  }

  protected String getSubjectName(String registryUrl) {
    final String[] registryUrlArr = registryUrl.split("/");
    return registryUrlArr[registryUrlArr.length - 3];

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

