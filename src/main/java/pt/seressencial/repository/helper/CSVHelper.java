package pt.seressencial.repository.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class CSVHelper {

 public static ByteArrayInputStream 
          employeesToCSV(List<PagespeedApiPagespeedResponseV5> responses) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("URL", "Performance", "Accessibility", "Best Practices", "SEO", "PWA");

        try (

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (PagespeedApiPagespeedResponseV5 response : responses) {
                List<String> data = Arrays.asList(response.getId(),
                        checkNull(() -> response.getLighthouseResult().getCategories().getPerformance().getScore()),
                        checkNull(() -> response.getLighthouseResult().getCategories().getAccessibility().getScore()),
                        checkNull(() -> response.getLighthouseResult().getCategories().getBestPractices().getScore()),
                        checkNull(() -> response.getLighthouseResult().getCategories().getSeo().getScore()),
                        checkNull(() -> response.getLighthouseResult().getCategories().getPwa().getScore()));
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    private static String checkNull(Supplier<Object> score) {
        try {
            return String.valueOf(Objects.requireNonNull(score.get()));
        } catch (NullPointerException e) {
            return "";
        }
    }
}