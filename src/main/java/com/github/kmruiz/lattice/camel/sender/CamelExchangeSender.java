package com.github.kmruiz.lattice.camel.sender;

import io.vlingo.lattice.exchange.ExchangeSender;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.engine.DefaultProducerTemplate;

import java.util.UUID;

/**
 * Class that allows an actor to send a message through a CamelExchange.
 * This class is mostly used in the CamelExchange class.
 *
 * @param <T>
 */
public class CamelExchangeSender<T> implements ExchangeSender<T>, AutoCloseable {
    private final String entrypoint;
    private final ProducerTemplate producerTemplate;


    public CamelExchangeSender(String endpoint, CamelContext context) throws Exception {
        // all messages will be sent to this seda queue. SEDA queues are asynchronous and ordered.
        this.entrypoint = "seda:" + UUID.randomUUID().toString();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // This route will read all messages from the entrypoint seda queue
                // and then sent to the final endpoint (SQS, ActiveMQ, Rabbit, whatever).
                // This allows us to buffer, aggregate or process messages before sending them to the exchange
                // implementation.

                from(entrypoint).to(endpoint);
            }
        });

        this.producerTemplate = new DefaultProducerTemplate(context);
        this.producerTemplate.start();
    }

    public void send(T message) {
        this.producerTemplate.sendBody(entrypoint, message);
    }

    @Override
    public void close() throws Exception {
        this.producerTemplate.stop();
    }
}
