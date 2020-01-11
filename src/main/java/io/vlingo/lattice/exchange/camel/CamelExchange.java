// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel;

import org.apache.camel.CamelContext;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.Forwarder;
import io.vlingo.lattice.exchange.camel.consumer.CamelExchangeConsumer;
import io.vlingo.lattice.exchange.camel.consumer.ExchangeConsumers;

public class CamelExchange implements Exchange {
  private final CamelContext camelContext;
  private final String name;
  private final CamelExchangeConsumer channel;

  private final Forwarder forwarder;

  public CamelExchange(CamelContext camelContext, String name, String endpoint) throws Exception {
    this.camelContext = camelContext;
    this.name = name;
    this.forwarder = new Forwarder();
    this.channel = ExchangeConsumers.receivingFrom(endpoint, camelContext, this.forwarder::forwardToReceiver);
  }

  @Override
  public void close() {
    channel.close();
    camelContext.stop();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T channel() {
    return (T) channel;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T connection() {
    return (T) camelContext;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public <L, E, EX> Exchange register(Covey<L, E, EX> covey) {
    forwarder.register(covey);
    return this;
  }

  @Override
  public <L> void send(L l) {
    forwarder.forwardToSender(l);
  }
}
