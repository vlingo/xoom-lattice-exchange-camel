// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultConsumerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.DefaultRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class CamelTest {
    protected final long DEFAULT_TIMEOUT = 500; // ms

    private CamelContext context;
    private ProducerTemplate producerTemplate;
    private ConsumerTemplate consumerTemplate;
    private Registry registry;

    @BeforeEach
    public void initiateCamelContext() {
        registry = new DefaultRegistry();
        context = new DefaultCamelContext(registry);
        context.start();

        producerTemplate = new DefaultProducerTemplate(context);
        consumerTemplate = new DefaultConsumerTemplate(context);

        producerTemplate.start();
        consumerTemplate.start();
    }

    @AfterEach
    public void tearDownCamelContext() {
        producerTemplate.stop();
        consumerTemplate.stop();

        context.stop();
    }

    // The CamelContext is where all routes and dependencies are set up, and accessible.
    // A camel context is isolated from the external world.
    // It will be needed to build the route that will send the exchange to the touchpoint (like ActiveMQ)
    protected final CamelContext context() {
        return context;
    }

    // ProducerTemplates allows us to send exchanges to a consumer endpoint. For example, if we have the route:
    // seda:origin -> kafka:whatever
    // We can send a exchange to seda:origin that will be eventually handled by the route and end in kafka.
    protected final ProducerTemplate producer() {
        return producerTemplate;
    }

    // ConsumerTemplate allows us to receive exchanges from consumer endpoints.
    protected final ConsumerTemplate consumer() {
        return consumerTemplate;
    }

    // The registry is used for dependency injection. Beans defined in the registry can be used as bean references
    // in the configuration of endpoints.
    protected Registry camelRegistry() {
        return registry;
    }

}
