// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.integration;

import java.util.UUID;

import org.apache.camel.CamelContext;
import org.testcontainers.containers.GenericContainer;

import io.vlingo.xoom.lattice.exchange.camel.CamelTestWithDockerIntegration;

import static org.apache.camel.component.activemq.ActiveMQComponent.activeMQComponent;

@SuppressWarnings("rawtypes")
public class ActiveMQIntegrationTest extends CamelTestWithDockerIntegration {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    @SuppressWarnings("resource")
    protected GenericContainer testContainer() {
        return new GenericContainer("rmohr/activemq:5.15.9-alpine")
                .withExposedPorts(61616);
    }

    @Override
    protected String exchangeUri(GenericContainer activeMQ) {
        return String.format("activemq:queue:%s", QUEUE_NAME);
    }

    @Override
    protected void configureCamelContext(final CamelContext context, final GenericContainer activeMQ) {
        context.addComponent("activemq", activeMQComponent(String.format("auto://%s:%d", activeMQ.getHost(), activeMQ.getMappedPort(61616))));
    }
}
