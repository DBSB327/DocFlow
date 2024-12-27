package com.neoskat.docflow.extractor;

import java.io.IOException;

public interface ContentExtractor {
    String extract(String filePath) throws IOException;
}
