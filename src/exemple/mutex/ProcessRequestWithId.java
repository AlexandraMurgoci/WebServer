package exemple.mutex;

import config.Configuration;
import exemple.DataObject;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class ProcessRequestWithId implements Callable<DataObject> {
    private long entryData;
    private Configuration configuration;
    private SequenceGenerator sequenceGenerator;

    public ProcessRequestWithId(long entryData, Configuration configuration, SequenceGenerator sequenceGenerator) {
        this.entryData = entryData;
        this.configuration = configuration;
        this.sequenceGenerator = sequenceGenerator;
    }

    public ProcessRequestWithId(Configuration configuration, SequenceGenerator sequenceGenerator) {
        this.configuration = configuration;
        this.entryData = 5000L;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public DataObject call() throws Exception {
        sleep(entryData);

        //intructiuni returnare resursa catre browser
        return new DataObject(configuration.getListingFile(), sequenceGenerator.getNextSequence());
    }
}
