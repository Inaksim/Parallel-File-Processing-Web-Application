package com.inaksim;


import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
public class FileUploadController {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Tika tika = new Tika();

    public String handleFileUpload(@RequestParam("files")MultipartFile[] files, Model model) {
        List<Future<String>> futures = new ArrayList<>();

        for(MultipartFile file : files) {
            futures.add(executorService.submit(() -> processFile(file)));
        }

        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                results.add("Failed to process file: " + e.getMessage());
            }
        }

        model.addAttribute("results", results);
        return "result";
    }

    private String processFile(MultipartFile file) {
        try (InputStream stream = file.getInputStream()) {
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler();
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(stream, handler, metadata, new ParseContext());

            String fileInfo = "Processed file: " + file.getOriginalFilename();
            fileInfo += ", Type: " + metadata.get(Metadata.CONTENT_TYPE);
            fileInfo += ", Size: " + file.getSize() + " bytes";

            return fileInfo;
        } catch (Exception e) {
            return "Failed to process file: " + e.getMessage();
        }
    }
}
