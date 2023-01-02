// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.lattice.exchange.camel.adapter;

import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;

/**
 * Adapts the local messages of type {@code L} to exchange messages
 * of type {@code org.apache.camel.Exchange} that hold external type {@code E}.
 *
 * @param <L> the local object type
 * @param <E> the external object type
 */
public abstract class AbstractCamelExchangeAdapter<L, E> implements ExchangeAdapter<L, E, Exchange> {
  private static final String MESSAGE_TYPE_HEADER = "VlingoExchangeMessageType";
  private final CamelContext camelContext;
  private final Class<L> localMessageClass;

  public AbstractCamelExchangeAdapter(final CamelContext camelContext, final Class<L> localMessageClass) {
    this.camelContext = camelContext;
    this.localMessageClass = localMessageClass;
  }

  protected CamelContext camelContext() {
    return camelContext;
  }

  protected Class<L> localMessageClass() {
    return localMessageClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Exchange toExchange(final L localMessage) {
    return buildExchange(ExchangeBuilder.anExchange(camelContext), localMessage)
            .withHeader(MESSAGE_TYPE_HEADER, localMessageClass.getSimpleName())
            .build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean supports(final Object exchangeMessage) {
    if (exchangeMessage instanceof Exchange) {
      final Message message = ((Exchange) exchangeMessage).getMessage();
      final String type = message.getHeader(MESSAGE_TYPE_HEADER, String.class);
      if (type != null) {
        return localMessageClass.getSimpleName().equalsIgnoreCase(type);
      }
    }

    return false;
  }

  /**
   * Build an instance of {@code org.apache.camel.Exchange} using {@code org.apache.camel.builder.ExchangeBuilder}.
   *
   * @param builder      created {@code org.apache.camel.builder.ExchangeBuilder} instance to be filled
   * @param localMessage the L typed local message
   * @return {@code org.apache.camel.builder.ExchangeBuilder}, filled with all required fields to build an {@code org.apache.camel.Exchange}.
   */
  protected abstract ExchangeBuilder buildExchange(final ExchangeBuilder builder, final L localMessage);

}
