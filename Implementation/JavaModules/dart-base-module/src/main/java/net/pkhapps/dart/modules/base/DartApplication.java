package net.pkhapps.dart.modules.base;

import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point into all DART module applications. This will start up a WELD container and then run an idle
 * background thread until the application is terminated. Beans that needs to be bootstrapped should observe the
 * {@link org.jboss.weld.environment.se.events.ContainerInitialized} event.
 */
public class DartApplication implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DartApplication.class);

    private DartApplication() {
        Weld weld = new Weld();
        weld.initialize();
        // Container will be shutdown when the application exits
    }

    @Override
    public void run() {
        Thread idleThread = new Thread(() -> {
            LOGGER.info("Starting idle thread");
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException ex) {
                LOGGER.info("Idle thread interrupted");
            }
        }, "Idle thread");
        idleThread.start();
    }

    /**
     * Starts the application.
     *
     * @param args any command line arguments (currently ignored).
     */
    public static void main(String[] args) {
        new DartApplication().run();
    }
}
