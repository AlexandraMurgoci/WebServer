package exemple.returnareprinmain;

import config.Configuration;
import exemple.DataObject;
import exemple.mutex.SequenceGenerator;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class ProcessRequestWithIdThatNotifiesListener implements Callable<DataObject> {
    private long entryData;
    private Configuration configuration;
    private SequenceGenerator sequenceGenerator;
    private ListenerThreadComplete listener;

    public ProcessRequestWithIdThatNotifiesListener(long entryData, Configuration configuration, SequenceGenerator sequenceGenerator, ListenerThreadComplete listener) {
        this.entryData = entryData;
        this.configuration = configuration;
        this.sequenceGenerator = sequenceGenerator;
        this.listener = listener;
    }

    public ProcessRequestWithIdThatNotifiesListener(Configuration configuration, SequenceGenerator sequenceGenerator, ListenerThreadComplete listener) {
        this.configuration = configuration;
        this.entryData = 5000L;
        this.sequenceGenerator = sequenceGenerator;
        this.listener = listener;
    }

    @Override
    public DataObject call() throws Exception {
        sleep(entryData);
        DataObject data = new DataObject(configuration.getListingFile(), sequenceGenerator.getNextSequence());
        listener.threadComplete(data);
        return data;
    }
}
