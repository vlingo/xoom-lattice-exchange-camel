package com.github.kmruiz.lattice.camel.sender;

import io.vlingo.lattice.exchange.ExchangeSender;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.model.RouteBuilderDefinition;
import org.apache.camel.model.RouteDefinition;

import java.util.UUID;

public class CamelExchangeSender<T> implements ExchangeSender<T>, AutoCloseable {
    private final String entrypoint;
    private final ProducerTemplate producerTemplate;

    public CamelExchangeSender(String endpoint, CamelContext context) throws Exception {
        this.entrypoint = "seda:" + UUID.randomUUID().toString();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
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
