// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.e2e;

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MockMessageReceiver<T> implements ExchangeReceiver<T> {
  private final AccessSafely accessSafely;
  private final Queue<Object> results = new ConcurrentLinkedQueue<>();

  public MockMessageReceiver(final int expectedMessages) {
    accessSafely = AccessSafely.afterCompleting(expectedMessages);
    accessSafely.writingWith("results", results::add);
    accessSafely.readingWith("results", () -> results);
  }

  @Override
  public void receive(final T message) {
    accessSafely.writeUsing("results", message);
  }


  public Queue<T> getResults(){
    return accessSafely.readFrom("results");
  }
}
