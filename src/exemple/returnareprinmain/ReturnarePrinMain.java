package exemple.returnareprinmain;

import config.Configuration;
import config.ConfigurationParser;
import executor.ExecutorServiceWrapper;
import exemple.mutex.SequenceGenerator;
import exemple.mutex.SequenceGeneratorUsingReentrantLock;

import java.util.stream.IntStream;

public class ReturnarePrinMain {
    private static Configuration configuration;
    private static ReturnResultsService returnResultsService = new ReturnResultsService();

    public static void main(String[] args) {
        configuration = new ConfigurationParser().parse();

        //surse utile
        // https://www.baeldung.com/java-executor-service-tutorial
        // https://www.baeldung.com/java-mutex
        ExecutorServiceWrapper executor = new ExecutorServiceWrapper(configuration.getMaxThreads());
        SequenceGenerator sequenceGeneratorCuMutex = new SequenceGeneratorUsingReentrantLock();
        IntStream.range(1,5).forEach((i) -> executor.submit(new ProcessRequestWithIdThatNotifiesListener(configuration, sequenceGeneratorCuMutex, returnResultsService)));
        IntStream.range(1,5).forEach((i) -> executor.submit(new ProcessRequestWithIdThatNotifiesListener(3000L, configuration, sequenceGeneratorCuMutex, returnResultsService)));

        executor.shutdown();
    }


}
