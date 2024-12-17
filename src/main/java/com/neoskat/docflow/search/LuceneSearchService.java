package com.neoskat.docflow.search;

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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LuceneSearchService {

    private final Directory index = new RAMDirectory();
    private final StandardAnalyzer analyzer = new StandardAnalyzer();


    public String pdfExtractor(String filePath) throws IOException {
            PDDocument doc = PDDocument.load(new File(filePath));
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(doc);

    }

    public void indexDocument(String filePath, String fileName) throws IOException {
        if(!(filePath.endsWith(".pdf") || filePath.endsWith(".txt"))){
            throw new IllegalArgumentException("Invalid file format");
        }
        String content;

        if(filePath.endsWith(".pdf")){
            content = pdfExtractor(filePath);
        }
        else{
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        }

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new TextField("fileName", fileName, Field.Store.YES));
            doc.add(new TextField("filePath", filePath, Field.Store.YES));  // Добавляем путь к файлу в индекс
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
