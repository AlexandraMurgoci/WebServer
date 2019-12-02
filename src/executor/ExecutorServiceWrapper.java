package executor;

import java.util.concurrent.*;

public class ExecutorServiceWrapper {

    private final ExecutorService executor;

    public ExecutorServiceWrapper(int nrMaxThreads) {
        executor = Executors.newFixedThreadPool(nrMaxThreads);
    }

    public <V> Future<V> submit(Callable<V> task) {
        return executor.submit(task);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownWait(long timeout) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
