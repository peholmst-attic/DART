package net.pkhapps.dart.modules.accounts;

import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point into the DART Accounts Application.
 */
public class AccountsApp implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsApp.class);

    private AccountsApp() {
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

    public static void main(String[] args) throws Exception {
        new AccountsApp().run();
    }
}
