/*
 * Copyright 2002-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.extensions.bigquery.unit.writer.loadjob.parquet.builder;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.FormatOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.WriteChannelConfiguration;
import org.apache.avro.Schema;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.extensions.bigquery.common.PersonDto;
import org.springframework.batch.extensions.bigquery.common.TestConstants;
import org.springframework.batch.extensions.bigquery.unit.base.AbstractBigQueryTest;
import org.springframework.batch.extensions.bigquery.writer.loadjob.BigQueryLoadJobBaseItemWriter;
import org.springframework.batch.extensions.bigquery.writer.loadjob.parquet.BigQueryLoadJobParquetItemWriter;
import org.springframework.batch.extensions.bigquery.writer.loadjob.parquet.builder.BigQueryLoadJobParquetItemWriterBuilder;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

class BigQueryLoadJobParquetItemWriterBuilderTest extends AbstractBigQueryTest {

	@Test
	void testBuild() throws IllegalAccessException, NoSuchFieldException {
		MethodHandles.Lookup parquetWriterHandle = MethodHandles.privateLookupIn(BigQueryLoadJobParquetItemWriter.class,
				MethodHandles.lookup());
		MethodHandles.Lookup baseWriterHandle = MethodHandles.privateLookupIn(BigQueryLoadJobBaseItemWriter.class,
				MethodHandles.lookup());

		DatasetInfo datasetInfo = DatasetInfo.newBuilder(TestConstants.DATASET).setLocation("europe-west-2").build();
		Consumer<Job> jobConsumer = job -> {
		};
		BigQuery mockedBigQuery = prepareMockedBigQuery();
		Schema schema = PersonDto.getAvroSchema();
		CompressionCodecName codecName = CompressionCodecName.BROTLI;

		WriteChannelConfiguration writeConfiguration = WriteChannelConfiguration
			.newBuilder(TableId.of(datasetInfo.getDatasetId().getDataset(), TestConstants.PARQUET))
			.setFormatOptions(FormatOptions.parquet())
			.build();

		BigQueryLoadJobParquetItemWriter writer = new BigQueryLoadJobParquetItemWriterBuilder().schema(schema)
			.codecName(codecName)
			.writeChannelConfig(writeConfiguration)
			.jobConsumer(jobConsumer)
			.bigQuery(mockedBigQuery)
			.datasetInfo(datasetInfo)
			.build();

		Assertions.assertNotNull(writer);

		Schema actualSchema = (Schema) parquetWriterHandle
			.findVarHandle(BigQueryLoadJobParquetItemWriter.class, "schema", Schema.class)
			.get(writer);

		CompressionCodecName actualCodecName = (CompressionCodecName) parquetWriterHandle
			.findVarHandle(BigQueryLoadJobParquetItemWriter.class, "codecName", CompressionCodecName.class)
			.get(writer);

		WriteChannelConfiguration actualWriteChannelConfig = (WriteChannelConfiguration) parquetWriterHandle
			.findVarHandle(BigQueryLoadJobParquetItemWriter.class, "writeChannelConfig",
					WriteChannelConfiguration.class)
			.get(writer);

		Consumer<Job> actualJobConsumer = (Consumer<Job>) baseWriterHandle
			.findVarHandle(BigQueryLoadJobBaseItemWriter.class, "jobConsumer", Consumer.class)
			.get(writer);

		BigQuery actualBigQuery = (BigQuery) baseWriterHandle
			.findVarHandle(BigQueryLoadJobBaseItemWriter.class, "bigQuery", BigQuery.class)
			.get(writer);

		DatasetInfo actualDatasetInfo = (DatasetInfo) baseWriterHandle
			.findVarHandle(BigQueryLoadJobParquetItemWriter.class, "datasetInfo", DatasetInfo.class)
			.get(writer);

		Assertions.assertEquals(schema, actualSchema);
		Assertions.assertEquals(codecName, actualCodecName);
		Assertions.assertEquals(writeConfiguration, actualWriteChannelConfig);
		Assertions.assertEquals(jobConsumer, actualJobConsumer);
		Assertions.assertEquals(mockedBigQuery, actualBigQuery);
		Assertions.assertEquals(datasetInfo, actualDatasetInfo);
	}

}