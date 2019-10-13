// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.lattice.exchange.camel;

import io.vlingo.lattice.exchange.ExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;

/**
 * Adapts the local messages of type {@code L} to exchange messages
 * of type {@code org.apache.camel.Exchange} that hold external type {@code E}.
 *
 * @param <L> the local object type
 * @param <E> the external object type
 */
public abstract class CamelExchangeAdapter<L, E> implements ExchangeAdapter<L, E, Exchange> {
  private final CamelContext camelContext;

  public CamelExchangeAdapter(final CamelContext camelContext) {this.camelContext = camelContext;}

  /**
   * {@inheritDoc}
   */
  @Override
  public final Exchange toExchange(final L localMessage) {
    return buildExchange(ExchangeBuilder.anExchange(camelContext), localMessage).build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean supports(final Object exchangeMessage) {
    return exchangeMessage instanceof Exchange;
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
