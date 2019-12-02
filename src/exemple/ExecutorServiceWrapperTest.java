package exemple;

import config.Configuration;
import config.ConfigurationParser;
import executor.ExecutorServiceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class ExecutorServiceWrapperTest {
    private static Configuration configuration;
    public static void main(String[] args) {
        configuration = new ConfigurationParser().parse();

        System.out.println(configuration);
        //surse utile
        // https://www.baeldung.com/java-executor-service-tutorial
        // https://www.baeldung.com/java-mutex
        ExecutorServiceWrapper executor = new ExecutorServiceWrapper(configuration.getMaxThreads());
        List<Future> list = new ArrayList<>();
        IntStream.range(1,15).forEach((i) -> list.add(executor.submit(new ProcessRequest(configuration))));
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
