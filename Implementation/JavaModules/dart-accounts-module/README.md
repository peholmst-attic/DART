# DART Accounts Module

This module is contains the user account management of DART. It plugs into RabbitMQ through the 
[rabbitmq-auth-backend-amqp](https://github.com/rabbitmq/rabbitmq-auth-backend-amqp) plugin. This plugin may not be
ready for serious production use yet, but it suits the DART project fine because one of the main design principles of
DART is to only use RabbitMQ for communication between components. Also, since DART is only a hobby project and not 
intended to be used "for real", it does not matter that the plugin is still in development. 

User accounts are stored in a PostgreSQL database and accessed through JOOQ. The schema is created using Flyway.

## Setting up the database

1. Create a new PostgreSQL database. Either use the credentials defined in [pom.xml](dart-accounts-app/pom.xml) or
   edit the file so that the JDBC URL, username and password are correct.
2. Inside [dart-accounts-app](dart-accounts-app), run `mvn flyway:migrate` to create the database schema. 
   DART Accounts Module uses its own schema so the database need not even be empty.

## Building

1. Build and install [dart-base-module](../dart-base-module) if you have not already.
2. Run `mvn clean install` to build the module. This will also generate the JOOQ classes from the newly created database
   schema and a ZIP-file for distribution.

## Configuring RabbitMQ

1. Create a new virtual host for DART.
2. Create a new local user (with remote access and a secure password) that the DART Accounts Module will use when 
   connecting to RabbitMQ.
3. Create a new local user (without remote access, such as 'guest') that the authentication backend will use when 
   connecting to RabbitMQ.
4. Download, install and enable the 
   [rabbitmq-auth-backend-amqp](https://github.com/rabbitmq/rabbitmq-auth-backend-amqp) plugin.
5. Configure RabbitMQ to use both the internal authentication backend and the AMQP authentication backend. For example,
   the following *rabbitmq.config* will do the trick if the username created in step 3 is 'guest' and the virtual host
   created in step 1 is 'dart':
```
   [
     {rabbit, [{auth_backends, [rabbit_auth_backend_internal,
                                rabbit_auth_backend_amqp]}]},
     {rabbitmq_auth_backend_amqp,
      [{username, <<"guest">>},
       {vhost,    <<"dart">>},
       {exchange, <<"authentication">>}]}
   ].
```

## Trying it out in the IDE

1. Make a copy of [config.properties.samle](dart-accounts-app/src/main/resources/config.properties.sample) and rename it
   to *config.properties*. Make the necessary changes for your environment. This file will must not be checked into GIT,
   nor will it be included in the final JAR.
2. Run `net.pkhapps.dart.modules.base.DartApplication`.
3. Run [AccountsAppIntegrationTest](dart-accounts-app/src/test/java/net/pkhapps/dart/modules/accounts/AccountsAppIntegrationTest.java).
   If everything is set up as it should be, the test should pass.
   
## Running from the distribution (Linux/OS X)

1. After building, copy *target/dart-accounts-app-VERSION-full.zip* into a suitable directory and unzip it.
2. Make the necessary changes to *dart-accounts-app-config.properties*. If you want to use a separate configuration 
   file, you can edit the shell script to point to it.
3. Start the application by running the shell script *dart-accounts-app.sh*.

