package config;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigurationParser {
    private String relativePath;
    private Configuration configuration;

    public ConfigurationParser() {
        relativePath = "";
    }

    public ConfigurationParser(String relativePath) {
        this.relativePath = relativePath;
    }

    public Configuration parse() {
        this.configuration = new Configuration();

        this.relativePath = this.relativePath + "Config";

        parseConfigFile();
        parseRedirectsFile();
        parseListingFile();

        return configuration;
    }

    private void parseConfigFile() {
        String configFile = this.relativePath + "\\config.txt";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(configFile));
            boolean readSites = false;
            while( (line = br.readLine()) != null){
                if(line.length()!=0 && line.charAt(0)!='#' && line.trim().length()!=0) {
                    if(!readSites) {
                        if(!line.contains(":")) throw new InvalidStateException("Line should containt a token separated by :");
                        String[] tokenAndValue = line.split(":");
                        switch (tokenAndValue[0].trim()) {
                            case ConfigurationTokens.configTokens.PORT:
                                int port = Integer.parseInt(tokenAndValue[1].trim());
                                configuration.setPort(port);
                                break;
                            case ConfigurationTokens.configTokens.MAX_THREADS:
                                int maxThreads = Integer.parseInt(tokenAndValue[1].trim());
                                configuration.setMaxThreads(maxThreads);
                                break;
                            case ConfigurationTokens.configTokens.LOG_LEVEL:
                                int logLevel = Integer.parseInt(tokenAndValue[1].trim());
                                configuration.setLogLevel(logLevel);
                                break;
                            case ConfigurationTokens.configTokens.REDIRECTS:
                                String redirectsPath = this.relativePath + (tokenAndValue[1].trim());
                                configuration.setRedirectsFile(redirectsPath);
                                break;
                            case ConfigurationTokens.configTokens.LISTING:
                                String listingPath = this.relativePath + (tokenAndValue[1].trim());
                                configuration.setListingFile(listingPath);
                                break;
                            case ConfigurationTokens.configTokens.SITES:
                                readSites = true;
                        }
                    }
                    else {
                        if(!line.trim().contains(" "))throw new InvalidStateException("Line should containt a site name and it's location separated by ' '");
                        String[] siteAndPath = line.split(" ");
                        configuration.getSitesToSitesPath().put(siteAndPath[0], siteAndPath[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Oops! Please check for the presence of file in the path specified.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Oops! Unable to read the file.");
            e.printStackTrace();
        }
        finally {
            try {
                if(br!=null)br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseRedirectsFile() {
        BufferedReader br = null;
        String line = "";
        List<String> redirectsList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(configuration.getRedirectsFile()));
            while( (line = br.readLine()) != null){
                if(line.length()!=0 && line.charAt(0)!='#' && line.trim().length()!=0) {
                    String[] redirectsArray = line.split(" ");
                    for(String redirect : redirectsArray) {
                        if(redirect.length()!=0) redirectsList.add(redirect);
                    }
                }
            }
            CopyOnWriteArrayList<String> redirects = new CopyOnWriteArrayList<>(redirectsList);

            configuration.setRedirects(redirects);
        } catch (FileNotFoundException e) {
            System.err.println("Oops! Please check for the presence of file in the path specified.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Oops! Unable to read the file.");
            e.printStackTrace();
        }
        finally {
            try {
                if(br!=null)br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseListingFile() {
        BufferedReader br = null;
        String line = "";
        ConcurrentMap<String, CopyOnWriteArrayList<String>> sitesToListings = new ConcurrentHashMap<>();
        try {
            br = new BufferedReader(new FileReader(configuration.getListingFile()));
            while( (line = br.readLine()) != null){
                if(line.length()!=0 && line.charAt(0)!='#' && line.trim().length()!=0) {
                    String[] listingArray = line.split(" ");
                    if(listingArray.length!=0 && listingArray[0].length()!=0) {
                        String site = listingArray[0];
                        List<String> listingList = new ArrayList<>();
                        for (String listing : listingArray) {
                            if (listing.length() != 0) listingList.add(listing);
                        }
                        listingList.remove(0);
                        CopyOnWriteArrayList<String> listings = new CopyOnWriteArrayList<>(listingList);
                        sitesToListings.put(site, listings);
                    }
                }
            }

            configuration.setSitesToListings(sitesToListings);
        } catch (FileNotFoundException e) {
            System.err.println("Oops! Please check for the presence of file in the path specified.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Oops! Unable to read the file.");
            e.printStackTrace();
        }
        finally {
            try {
                if(br!=null)br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ConfigurationTokens {
        private static class configTokens {
            private static final String PORT = "Port";
            private static final String MAX_THREADS = "MaxThreads";
            private static final String LOG_LEVEL = "LogLevel";
            private static final String REDIRECTS = "Redirects";
            private static final String LISTING = "Listing";
            private static final String SITES = "Sites";
        }
    }
}
