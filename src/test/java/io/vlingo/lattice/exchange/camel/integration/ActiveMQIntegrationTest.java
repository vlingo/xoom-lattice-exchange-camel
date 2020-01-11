// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.integration;

import java.util.UUID;

import org.testcontainers.containers.GenericContainer;

import io.vlingo.lattice.exchange.camel.CamelTestWithDockerIntegration;

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
        return String.format("activemq:queue:%s?brokerURL=auto://%s:%d", QUEUE_NAME, activeMQ.getContainerIpAddress(), activeMQ.getMappedPort(61616));
    }
}
