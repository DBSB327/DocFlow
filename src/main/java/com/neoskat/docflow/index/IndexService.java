package com.neoskat.docflow.index;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {
    private final Directory index = new RAMDirectory();
    private final StandardAnalyzer analyzer = new StandardAnalyzer();

    public void indexDocument(String content, String fileName, String filePath) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new TextField("fileName", fileName, Field.Store.YES));
            doc.add(new TextField("filePath", filePath, Field.Store.YES));
            writer.addDocument(doc);
        }
    }

    public List<String> searchDocuments(String queryStr) throws Exception {
        List<String> results = new ArrayList<>();
        Query query = new QueryParser("content", analyzer).parse(queryStr);

        try (DirectoryReader reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                results.add(doc.get("fileName"));
            }
        }
        return results;
    }
}

