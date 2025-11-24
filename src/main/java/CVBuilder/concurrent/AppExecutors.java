package CVBuilder.concurrent;

import java.util.concurrent.*;

public final class AppExecutors {

    private AppExecutors() {}


    public static final ExecutorService DB_EXECUTOR = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors()),
            new NamedThreadFactory("cv-db-worker")
    );


    public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(
            1,
            new NamedThreadFactory("cv-scheduler")
    );

    private static class NamedThreadFactory implements ThreadFactory {
        private final String base;
        private final ThreadFactory delegate = Executors.defaultThreadFactory();
        private int counter = 0;

        NamedThreadFactory(String base) {
            this.base = base;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = delegate.newThread(r);
            t.setName(base + "-" + (++counter));
            t.setDaemon(true);
            return t;
        }
    }
}
