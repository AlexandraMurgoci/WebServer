import config.Configuration;
import config.ConfigurationParser;
import executor.ExecutorServiceWrapper;

public class Main {
    private static Configuration configuration;

    public static void main(String[] args) {
        configuration = new ConfigurationParser().parse();
        //surse utile
        // https://www.baeldung.com/java-executor-service-tutorial
        // https://www.baeldung.com/java-mutex
        ExecutorServiceWrapper executor = new ExecutorServiceWrapper(configuration.getMaxThreads());

    }
}
