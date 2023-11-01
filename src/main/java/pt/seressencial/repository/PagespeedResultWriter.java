package pt.seressencial.repository;

import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import pt.seressencial.repository.helper.CSVHelper;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Singleton
public class PagespeedResultWriter {

        private static final String FILENAME = "%s\\pagespeed-results-%s.csv";
        private static final String DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";

        public void write(List<PagespeedApiPagespeedResponseV5> response, String outputDirectory) throws IOException {
                String filenameWithDate = String.format(FILENAME, outputDirectory, OffsetDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
                log.info("Writing results to {}", filenameWithDate);
                Files.createDirectories(Paths.get(outputDirectory));
                try (FileWriter fileWriter = new FileWriter(filenameWithDate)) {
                        ByteArrayInputStream in = CSVHelper.employeesToCSV(response);
                        byte[] bytes = in.readAllBytes();
                        //noinspection ResultOfMethodCallIgnored
                        in.read(bytes);
                        String csv = new String(bytes, StandardCharsets.UTF_8);
                        fileWriter.write(csv);
                } catch (IOException e) {
                        throw new IOException("Error writing to file", e);
                }
                log.info("Successfully wrote results to {}", filenameWithDate);
        }
}
