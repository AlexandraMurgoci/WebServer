package config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Configuration {
    private int port;
    private int maxThreads;
    private int logLevel;

    private String RedirectsFile;
    private String ListingFile;

    private ConcurrentMap<String, String> sitesToSitesPath = new ConcurrentHashMap<>();

    private CopyOnWriteArrayList<String> redirects = new CopyOnWriteArrayList<>();
    private ConcurrentMap<String, CopyOnWriteArrayList<String>> sitesToListings = new ConcurrentHashMap<>();

    private String relativePath;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getRedirectsFile() {
        return RedirectsFile;
    }

    public void setRedirectsFile(String redirectsFile) {
        RedirectsFile = redirectsFile;
    }

    public String getListingFile() {
        return ListingFile;
    }

    public void setListingFile(String listingFile) {
        ListingFile = listingFile;
    }

    public ConcurrentMap<String, String> getSitesToSitesPath() {
        return sitesToSitesPath;
    }

    public void setSitesToSitesPath(ConcurrentMap<String, String> sitesToSitesPath) {
        this.sitesToSitesPath = sitesToSitesPath;
    }

    public CopyOnWriteArrayList<String> getRedirects() {
        return redirects;
    }

    public void setRedirects(CopyOnWriteArrayList<String> redirects) {
        this.redirects = redirects;
    }

    public ConcurrentMap<String, CopyOnWriteArrayList<String>> getSitesToListings() {
        return sitesToListings;
    }

    public void setSitesToListings(ConcurrentMap<String, CopyOnWriteArrayList<String>> sitesToListings) {
        this.sitesToListings = sitesToListings;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "port=" + port +
                ", maxThreads=" + maxThreads +
                ", logLevel=" + logLevel +
                ", RedirectsFile='" + RedirectsFile + '\'' +
                ", ListingFile='" + ListingFile + '\'' +
                ", sitesToSitesPath=" + sitesToSitesPath +
                ", redirects=" + redirects +
                ", sitesToListings=" + sitesToListings +
                ", relativePath='" + relativePath + '\'' +
                '}';
    }
}
