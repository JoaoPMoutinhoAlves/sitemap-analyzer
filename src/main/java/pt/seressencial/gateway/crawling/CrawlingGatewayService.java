package pt.seressencial.gateway.crawling;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapIndex;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.UnknownFormatException;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Slf4j
public class CrawlingGatewayService {

    public SiteMap getSitemap(String urlString) throws IOException, UnknownFormatException, URISyntaxException {
        AbstractSiteMap abstractSiteMap = getAbstractSiteMap(urlString);
        if(abstractSiteMap instanceof SiteMap) {
            log.info("Successfully downloaded sitemap.");
            return (SiteMap) abstractSiteMap;
        } else if(abstractSiteMap instanceof SiteMapIndex) {
            log.warn("Error downloading sitemap. Sitemap is a sitemap index.");
            throw new IllegalArgumentException("URL is a sitemap index");
        }
        log.warn("Error downloading sitemap. Sitemap is of an unknown type.");
        throw new IllegalArgumentException("Unknown sitemap type");
    }

    private AbstractSiteMap getAbstractSiteMap(String urlString) throws URISyntaxException, UnknownFormatException, IOException {
        SiteMapParser parser = new SiteMapParser();
        String contentType = MediaType.APPLICATION_XML;
        log.info("Downloading sitemap from {}", urlString);
        byte[] content = createCrawlingGateway(urlString).getSitemap().getBytes(StandardCharsets.UTF_8);
        URL url = new URI(urlString).toURL();
        return parser.parseSiteMap(contentType, content, url);
    }

    private CrawlingGateway createCrawlingGateway(String host) {
        return QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(host))
                .build(CrawlingGateway.class);
    }
}
