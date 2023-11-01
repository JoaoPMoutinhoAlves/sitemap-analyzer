package pt.seressencial;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import pt.seressencial.service.SitemapCrawlerService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@QuarkusMain
public class SerEssencialApplication {

    public static void main(String... args) {
        Quarkus.run(SitemapCrawlerService.class, args);
        new BufferedReader(new InputStreamReader(System.in));
        Quarkus.waitForExit();
    }
}