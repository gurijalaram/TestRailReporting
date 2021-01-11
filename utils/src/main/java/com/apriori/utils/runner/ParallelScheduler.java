package com.apriori.utils.runner;

import org.junit.runners.model.RunnerScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author kpatel
 */
class ParallelScheduler implements RunnerScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelScheduler.class);

    private ExecutorService executor;

    public ParallelScheduler(String threadCount) {
        LOGGER.debug("ThreadPoolScheduler constructor start");

        int numThreads = Integer.parseInt(threadCount);
        LOGGER.debug("ThreadPoolScheduler number of threads: " + numThreads);
        executor = Executors.newFixedThreadPool(numThreads);
        LOGGER.debug("ThreadPoolScheduler constructor end");
    }

    public void finished() {
        LOGGER.debug("ThreadPoolScheduler finished start");
        executor.shutdown();
        try {
            executor.awaitTermination(500, TimeUnit.MINUTES);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
        LOGGER.debug("ThreadPoolScheduler finished end");
    }

    public void schedule(Runnable childStatement) {
        LOGGER.debug("ThreadPoolScheduler schedule start");
        executor.submit(childStatement);
        LOGGER.debug("ThreadPoolScheduler schedule end");
    }
}
