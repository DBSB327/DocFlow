package com.neoskat.docflow.service;

import com.neoskat.docflow.extractor.ContentExtractor;
import com.neoskat.docflow.extractor.ContentExtractorFactory;
import com.neoskat.docflow.index.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LuceneSearchService {
    private final ContentExtractorFactory extractorFactory;
    private final IndexService indexService;

    public void indexFile(String filePath, String fileName) throws IOException {
        ContentExtractor extractor = extractorFactory.getExtractor(filePath);
        String content = extractor.extract(filePath);
        indexService.indexDocument(content, fileName, filePath);
    }

    public List<String> searchDocuments(String queryStr) throws Exception {
        return indexService.searchDocuments(queryStr);
    }
}

