package exemple.mutex;

public class SequenceGenerator {
    private int currentValue = 0;

    public SequenceGenerator() {
    }

    public int getNextSequence() {
        currentValue = currentValue + 1;
        return currentValue;
    }
}
