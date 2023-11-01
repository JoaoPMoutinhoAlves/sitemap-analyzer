package pt.seressencial.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pt.seressencial.gateway.crawling.CrawlingGatewayService;
import pt.seressencial.gateway.google.PagespeedGatewayService;
import pt.seressencial.repository.PagespeedResultWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ApplicationScoped
public class SitemapCrawlerService implements QuarkusApplication {

    @Inject
    CrawlingGatewayService crawlingGatewayService;
    @Inject
    PagespeedGatewayService pagespeedGatewayService;
    @Inject
    PagespeedResultWriter pagespeedResultWriter;
    SitemapCrawlerProperties properties;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public int run(String... args) throws IOException {
        DateTime start = new DateTime(System.currentTimeMillis());
        try {
            analyzeSitemap();
        } catch (Exception e) {
            log.error("Error during process:", e);
        }
        DateTime end = new DateTime(System.currentTimeMillis());
        log.info("Execution took {} minutes.", (end.getValue() - start.getValue()) / 60000);
        log.info("Press any key to exit");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        return 0;
    }

    private void analyzeSitemap() throws UnknownFormatException, IOException, URISyntaxException {
        this.properties = new SitemapCrawlerProperties().read();
        SiteMap sitemap = downloadSitemap();
        List<URL> sitemapUrls = getSiteMapUrls(sitemap);
        List<PagespeedApiPagespeedResponseV5> results = getPagespeedResults(sitemapUrls);
        writeResults(results);
    }

    private List<URL> getSiteMapUrls(SiteMap sitemap) {
        List<URL> urls = sitemap.getSiteMapUrls().stream()
                .map(SiteMapURL::getUrl)
                .filter(url -> url.getFile().contains(properties.getSitemapUrlExtension()))
                .toList();
        log.info("Found {} urls in sitemap. Starting to download...", urls.size());
        return urls;
    }

    private SiteMap downloadSitemap() throws UnknownFormatException, IOException, URISyntaxException {
        return crawlingGatewayService.getSitemap(properties.getSitemapUrl());
    }

    private List<PagespeedApiPagespeedResponseV5> getPagespeedResults(List<URL> sitemapUrls) {
        int size = sitemapUrls.size();
        return sitemapUrls.parallelStream()
                .map(url -> {
                    PagespeedApiPagespeedResponseV5 response = pagespeedGatewayService.runPageSpeed(url.toString(), properties);
                    log.info("Downloaded {} of {} urls", counter.incrementAndGet(), size);
                    return response;
                })
                .toList();
    }

    private void writeResults(List<PagespeedApiPagespeedResponseV5> results) throws IOException {
        results = results.stream().sorted((o1, o2) -> Float.compare(Float.parseFloat(o1.getLighthouseResult().getCategories().getPerformance().getScore().toString()), Float.parseFloat(o2.getLighthouseResult().getCategories().getPerformance().getScore().toString()))).toList();
        pagespeedResultWriter.write(results, properties.getOutputDirectory());
    }

}
