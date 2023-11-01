package pt.seressencial.gateway.crawling;

import jakarta.ws.rs.GET;

public interface CrawlingGateway {

    @GET
    String getSitemap();

}
