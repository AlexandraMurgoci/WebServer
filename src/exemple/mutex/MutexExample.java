package exemple.mutex;

import config.Configuration;
import config.ConfigurationParser;
import executor.ExecutorServiceWrapper;
import exemple.ProcessRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class MutexExample {
    private static Configuration configuration;

    public static void main(String[] args) {
        configuration = new ConfigurationParser().parse();

        //surse utile
        // https://www.baeldung.com/java-executor-service-tutorial
        // https://www.baeldung.com/java-mutex
        ExecutorServiceWrapper executor = new ExecutorServiceWrapper(configuration.getMaxThreads());
        List<Future> list = new ArrayList<>();

        //fara mutex
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        IntStream.range(1,5).forEach((i) -> list.add(executor.submit(new ProcessRequestWithId(configuration, sequenceGenerator))));
        IntStream.range(1,5).forEach((i) -> list.add(executor.submit(new ProcessRequestWithId(3000L, configuration, sequenceGenerator))));
        list.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        //cu mutex
        System.out.println("\n\n\ncu mutex\n");
        list.clear();
        SequenceGenerator sequenceGeneratorCuMutex = new SequenceGeneratorUsingReentrantLock();
        IntStream.range(1,5).forEach((i) -> list.add(executor.submit(new ProcessRequestWithId(configuration, sequenceGeneratorCuMutex))));
        IntStream.range(1,5).forEach((i) -> list.add(executor.submit(new ProcessRequestWithId(3000L, configuration, sequenceGeneratorCuMutex))));
        list.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }
}
