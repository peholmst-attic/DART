package net.pkhapps.dart.modules.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * CDI provider that creates and destroys a {@link ScheduledExecutorService}.
 */
@ApplicationScoped
class ExecutorProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorProvider.class);

    @Produces
    @ApplicationScoped
    ScheduledExecutorService createExecutorService() {
        LOGGER.info("Creating executor service");
        return Executors.newSingleThreadScheduledExecutor();
    }

    void destroyExecutorService(@Disposes ScheduledExecutorService executorService) {
        LOGGER.info("Shutting down executor service [{}]", executorService);
        executorService.shutdown();
    }

}
