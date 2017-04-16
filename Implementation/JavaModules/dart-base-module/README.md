# DART Base Module

This module contains base classes for all DART Module Applications. It has intentionally been kept very light to prevent
modules from becoming too coupled and to make refactoring as easy as possible.

A DART Module Application is essentially a Java application that is connected to a RabbitMQ message broker and uses that
broker to send and receive messages (a type of message-based micro-service architecture if you like).

The base module provides the following features:

- WELD CDI environment
- Main entry point into the application (main class)
- Automatic connection to RabbitMQ
- Automatic reconnection when RabbitMQ goes down
- Configuration through Netflix Archaius
- An injectable `ScheduledExecutorService` (currently single-threaded)

## How to use

Using the base module is easy:
1. Add this module as a dependency to your module.
2. Configure your module as a CDI bean archive (add *beans.xml*).
3. Extend `RabbitMQChannelManager` for every class that needs access to a RabbitMQ channel.
4. Observe `org.jboss.weld.environment.se.events.ContainerInitialized` for any bootstrapping you may need.
5. Use `net.pkhapps.dart.modules.base.DartApplication` as your main class.
6. Make sure Archaius can pick up the necessary RabbitMQ configuration properties (see `RabbitMQProperties`).

More detailed instructions may be added in the future. Or not - this is, after all, a hobby project.