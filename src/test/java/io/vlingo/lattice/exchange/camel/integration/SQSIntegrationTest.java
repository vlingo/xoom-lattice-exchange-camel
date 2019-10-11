// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.integration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.vlingo.lattice.exchange.camel.CamelTestWithDockerIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.UUID;

public class SQSIntegrationTest extends CamelTestWithDockerIntegration<LocalStackContainer> {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSIntegrationTest.class);

    @Override
    protected LocalStackContainer testContainer() {
        return new LocalStackContainer()
                .withServices(LocalStackContainer.Service.SQS);
    }

    @Override
    protected String exchangeUri(LocalStackContainer localStack) {
        LOGGER.info("SQL container is {}", localStack.isRunning());
        
        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withClientConfiguration(new ClientConfiguration().withMaxErrorRetry(5))
                .build();

        camelRegistry().bind("client", sqs);
        return String.format("aws-sqs://%s?amazonSQSClient=#client&delay=5000&maxMessagesPerPoll=5", QUEUE_NAME);
    }
}
