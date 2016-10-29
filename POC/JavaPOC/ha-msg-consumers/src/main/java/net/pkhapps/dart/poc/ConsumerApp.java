package net.pkhapps.dart.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting up consumer 1");
        MessageConsumer consumer1 = new MessageConsumer("Consumer1");
        LOGGER.info("Starting up consumer 2");
        MessageConsumer consumer2 = new MessageConsumer("Consumer2");
    }
}
