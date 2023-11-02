package pt.seressencial.service;

import lombok.Data;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@ToString
public class SitemapCrawlerProperties {
    String sitemapUrl;
    String sitemapUrlExtension;
    String outputDirectory;
    String googleApiKey;
    List<String> pagespeedCategories;
    String pagespeedStrategy;

    public SitemapCrawlerProperties read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("").getAbsolutePath() + "\\application.properties", StandardCharsets.UTF_8));
        while(reader.ready()) {
            String line = reader.readLine();
            if(line.startsWith("sitemap.url=")) {
                sitemapUrl = line.substring("sitemap.url=".length());
            } else if(line.startsWith("sitemap.url.extension=")) {
                sitemapUrlExtension = line.substring("sitemap.url.extension=".length());
            } else if(line.startsWith("output.directory=")) {
                outputDirectory = line.substring("output.directory=".length());
            } else if(line.startsWith("google.api.key=")) {
                googleApiKey = line.substring("google.api.key=".length());
            } else if(line.startsWith("pagespeed.categories=")) {
                pagespeedCategories = List.of(line.substring("pagespeed.categories=".length()).split(","));
            } else if(line.startsWith("pagespeed.strategy=")) {
                pagespeedStrategy = line.substring("pagespeed.strategy=".length());
            }
        }
        return this;
    }

}
