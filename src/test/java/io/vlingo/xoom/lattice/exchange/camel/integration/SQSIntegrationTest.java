// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.integration;

import java.util.UUID;

import org.testcontainers.containers.localstack.LocalStackContainer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;

import io.vlingo.xoom.lattice.exchange.camel.CamelTestWithDockerIntegration;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

public class SQSIntegrationTest extends CamelTestWithDockerIntegration<LocalStackContainer> {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    @SuppressWarnings("resource")
    protected LocalStackContainer testContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.7.2"))
                .withServices(LocalStackContainer.Service.SQS);
    }

    @Override
    protected String exchangeUri(LocalStackContainer localStack) {
        SqsClient sqs = SqsClient.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                        )
                )
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.S3))
                .overrideConfiguration(
                        ClientOverrideConfiguration
                                .builder()
                                .retryPolicy(RetryPolicy.builder().numRetries(20).build())
                                .build()
                )
                .region(Region.US_WEST_1)
                .build();

        camelRegistry().bind("client", sqs);
        return String.format("aws2-sqs://%s?amazonSQSClient=#client&delay=5000&maxMessagesPerPoll=5&attributeNames=VlingoExchangeMessageType&messageAttributeNames=VlingoExchangeMessageType", QUEUE_NAME);
    }
}
