# vlingo-lattice-camel-exchange

PoC on how to use Apache Camel as a exchange transport for vlingo/lattice.

## How does it work?

It basically implements three classes:

* [ExchangeChannel](src/main/java/com/github/kmruiz/lattice/camel/channel/ExchangeChannel.java) used to read from a Camel endpoint. **[Test](src/test/java/com/github/kmruiz/lattice/camel/e2e/ExchangeChannelTest.java)**
* [CamelExchangeSender](src/main/java/com/github/kmruiz/lattice/camel/sender/CamelExchangeSender.java) used to write to a Camel endpoint.  **[Test](src/test/java/com/github/kmruiz/lattice/camel/e2e/ExchangeSenderTest.java)**
* [CamelExchange](src/main/java/com/github/kmruiz/lattice/camel/CamelExchange.java) main integration point to use Camel from actors.  **[Test](src/test/java/com/github/kmruiz/lattice/camel/e2e/CamelExchangeTest.java)**

You can also take a look at the integration tests with Docker, integration with:

* [ActiveMQ](src/test/java/com/github/kmruiz/lattice/camel/integration/ActiveMQIntegrationTest.java)
* [RabbitMQ](src/test/java/com/github/kmruiz/lattice/camel/integration/RabbitMQIntegrationTest.java)
* [SQS](src/test/java/com/github/kmruiz/lattice/camel/integration/SQSIntegrationTest.java)
