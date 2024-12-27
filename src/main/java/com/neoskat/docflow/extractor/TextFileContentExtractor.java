package com.neoskat.docflow.extractor;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class TextFileContentExtractor implements ContentExtractor {
    @Override
    public String extract(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
