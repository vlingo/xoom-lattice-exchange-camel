// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.integration;

import java.util.UUID;

import org.testcontainers.containers.localstack.LocalStackContainer;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import io.vlingo.xoom.lattice.exchange.camel.CamelTestWithDockerIntegration;
import org.testcontainers.utility.DockerImageName;

public class SQSIntegrationTest extends CamelTestWithDockerIntegration<LocalStackContainer> {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    @SuppressWarnings("resource")
    protected LocalStackContainer testContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.2"))
                .withServices(LocalStackContainer.Service.SQS);
    }

    @Override
    protected String exchangeUri(LocalStackContainer localStack) {
        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withClientConfiguration(new ClientConfiguration().withMaxErrorRetry(20))
                .build();

        camelRegistry().bind("client", sqs);
        return String.format("aws-sqs://%s?amazonSQSClient=#client&delay=5000&maxMessagesPerPoll=5&attributeNames=VlingoExchangeMessageType&messageAttributeNames=VlingoExchangeMessageType", QUEUE_NAME);
    }
}
