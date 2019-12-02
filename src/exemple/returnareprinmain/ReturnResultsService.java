package exemple.returnareprinmain;

public class ReturnResultsService implements ListenerThreadComplete{
    @Override
    public <V> void threadComplete(V result) {
        System.out.println(result);
    }
}
