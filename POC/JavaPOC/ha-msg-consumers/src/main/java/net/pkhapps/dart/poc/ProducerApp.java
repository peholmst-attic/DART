package net.pkhapps.dart.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApp.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting up producer");
        MessageProducer producer = new MessageProducer();
        producer.sendMessage(4);
        producer.close();
    }
}
