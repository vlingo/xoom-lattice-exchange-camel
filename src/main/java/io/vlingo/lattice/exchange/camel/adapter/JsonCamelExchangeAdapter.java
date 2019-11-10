// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.lattice.exchange.camel.adapter;

import io.vlingo.common.serialization.JsonSerialization;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;

public class JsonCamelExchangeAdapter<L, E> extends AbstractCamelExchangeAdapter<L, E> {
  
  public JsonCamelExchangeAdapter(final CamelContext camelContext, final Class<L> localMessageClass) {
    super(camelContext, localMessageClass);
  }

  @Override
  protected ExchangeBuilder buildExchange(final ExchangeBuilder builder, final L localMessage) {
    return builder.withBody(JsonSerialization.serialized(localMessage));
  }

  @Override
  public L fromExchange(final Exchange exchangeMessage) {
    return JsonSerialization.deserialized(exchangeMessage.getMessage().getBody(String.class), localMessageClass());
  }
}
