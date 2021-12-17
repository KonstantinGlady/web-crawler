package crawler;

public class Params {

    private final String internalScope;
    private int maxScopeSize = 1000;

    public Params(String url) {
        this.internalScope = Utils.getHost(url);
    }

    public String getInternalScope() {
        return internalScope;
    }

    public int getMaxScopeSize() {
        return maxScopeSize;
    }

    public void setMaxScopeSize(int maxScopeSize) {
        this.maxScopeSize = maxScopeSize;
    }
}
