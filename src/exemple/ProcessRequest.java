package exemple;

import config.Configuration;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class ProcessRequest implements Callable<DataObject> {

    private String entryData;
    private Configuration configuration;

    public ProcessRequest(String entryData, Configuration configuration) {
        this.entryData = entryData;
        this.configuration = configuration;
    }

    public ProcessRequest(Configuration configuration) {
        this.configuration = configuration;
        this.entryData = "default data";
    }

    @Override
    public DataObject call() throws Exception {
        sleep(5000);
        //intructiuni
        return new DataObject(configuration.getListingFile(), System.currentTimeMillis());
    }
}
