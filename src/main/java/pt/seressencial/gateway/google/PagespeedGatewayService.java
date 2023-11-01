package pt.seressencial.gateway.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.pagespeedonline.v5.PagespeedInsights;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import pt.seressencial.service.SitemapCrawlerProperties;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@ApplicationScoped
public class PagespeedGatewayService {

    SitemapCrawlerProperties properties;

    private static final String APPLICATION_NAME = "Crawler";
    private static final int READ_TIMEOUT = 120000;

    public PagespeedApiPagespeedResponseV5 runPageSpeed(String url, SitemapCrawlerProperties properties) {
        this.properties = properties;
        try {
            return getPagespeedApiPagespeedResponseV5(url);
        } catch (GeneralSecurityException | IOException e) {
            //Retry
            try {
                return getPagespeedApiPagespeedResponseV5(url);
            } catch (GeneralSecurityException | IOException ex) {
                log.error("Error running PageSpeed on {}", url, e);
                throw new RuntimeException(ex);
            }
        }
    }

    private PagespeedApiPagespeedResponseV5 getPagespeedApiPagespeedResponseV5(String url) throws GeneralSecurityException, IOException {
        HttpRequestInitializer httpRequestInitializer = request -> request.setReadTimeout(READ_TIMEOUT);
        PagespeedInsights pagespeedInsights = new PagespeedInsights.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), httpRequestInitializer).setApplicationName(APPLICATION_NAME).build();
        return pagespeedInsights.pagespeedapi().runpagespeed(url).setKey(properties.getGoogleApiKey()).setStrategy(properties.getPagespeedStrategy()).setCategory(properties.getPagespeedCategories()).execute();
    }
}
