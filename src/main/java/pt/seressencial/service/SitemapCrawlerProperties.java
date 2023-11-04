package pt.seressencial.service;

import lombok.Data;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Data
@ToString
public class SitemapCrawlerProperties {
    private static final String EQUALS = "=";
    private static final String SITEMAP_URL = "sitemap.url";
    private static final String SITEMAP_URL_EXTENSION = "sitemap.url.extension=";
    private static final String OUTPUT_DIRECTORY = "output.directory";
    private static final String GOOGLE_API_KEY = "google.api.key";
    private static final String PAGESPEED_CATEGORIES = "pagespeed.categories";
    private static final String PAGESPEED_STRATEGY = "pagespeed.strategy=";
    private static final String THREADS_MAX = "threads.max";
    String sitemapUrl;
    String sitemapUrlExtension;
    String outputDirectory;
    String googleApiKey;
    List<String> pagespeedCategories;
    String pagespeedStrategy;
    Integer maxThreads;

    public SitemapCrawlerProperties read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("").getAbsolutePath() + "\\properties.txt", StandardCharsets.UTF_8));
        while(reader.ready()) {
            String line = reader.readLine();
            if(line.startsWith(SITEMAP_URL + EQUALS)) {
                sitemapUrl = line.substring((SITEMAP_URL + EQUALS).length());
                throwIfEmpty(sitemapUrl, SITEMAP_URL);
            } else if(line.startsWith(SITEMAP_URL_EXTENSION)) {
                sitemapUrlExtension = line.substring(SITEMAP_URL_EXTENSION.length());
            } else if(line.startsWith(OUTPUT_DIRECTORY + EQUALS)) {
                outputDirectory = line.substring((OUTPUT_DIRECTORY + EQUALS).length());
                throwIfEmpty(outputDirectory, OUTPUT_DIRECTORY);
            } else if(line.startsWith(GOOGLE_API_KEY + EQUALS)) {
                googleApiKey = line.substring((GOOGLE_API_KEY + EQUALS).length());
                throwIfEmpty(googleApiKey, GOOGLE_API_KEY);
            } else if(line.startsWith(PAGESPEED_CATEGORIES + EQUALS)) {
                pagespeedCategories = List.of(line.substring((PAGESPEED_CATEGORIES + EQUALS).length()).split(","));
                throwIfEmpty(pagespeedCategories.toString(), PAGESPEED_CATEGORIES);
            } else if(line.startsWith(PAGESPEED_STRATEGY)) {
                pagespeedStrategy = line.substring(PAGESPEED_STRATEGY.length());
            } else if(line.startsWith(THREADS_MAX + EQUALS)) {
                try {
                    maxThreads = Integer.valueOf(line.substring((THREADS_MAX + EQUALS).length()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Property " + THREADS_MAX + " is not a number.");
                }
            }
        }
        return this;
    }

    private void throwIfEmpty(String value, String name) {
        if(Objects.isNull(value) || value.isEmpty()) {
            throw new IllegalArgumentException("Property " + name + " is empty.");
        }
    }

}
