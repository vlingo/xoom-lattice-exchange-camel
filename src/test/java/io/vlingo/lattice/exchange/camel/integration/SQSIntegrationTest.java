package io.vlingo.lattice.exchange.camel.integration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.vlingo.lattice.exchange.camel.CamelTestWithDockerIntegration;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.UUID;

public class SQSIntegrationTest extends CamelTestWithDockerIntegration<LocalStackContainer> {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    protected LocalStackContainer testContainer() {
        return new LocalStackContainer()
                .withServices(LocalStackContainer.Service.SQS);
    }

    @Override
    protected String exchangeUri(LocalStackContainer localStack) {
        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .build();

        camelRegistry().bind("client", sqs);
        return String.format("aws-sqs://%s?amazonSQSClient=#client&delay=5000&maxMessagesPerPoll=5", QUEUE_NAME);
    }
}
