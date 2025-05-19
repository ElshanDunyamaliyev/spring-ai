package dev.elshan.springai.sec03_RAG.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PgVectorLoader {

    private static final Logger log = LoggerFactory.getLogger(PgVectorLoader.class);
    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;


    @Value("classpath:/Azerbaijan_Constitution.pdf")
    private Resource resource;


    public PgVectorLoader(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

//    @PostConstruct
    public void init() {
        var count = jdbcClient
                .sql("select COUNT(*) from vector_store")
                .query(Integer.class)
                .single();

        log.info("Count of rows in vector store: {}", count);

        if (count == 0) {
            log.info("Initializing PG Vector Store Load!!");

            PdfDocumentReaderConfig config
                    = PdfDocumentReaderConfig
                    .builder()
                    .withPagesPerDocument(1)
                    .build();

            PagePdfDocumentReader reader
                    = new PagePdfDocumentReader(resource,config);

            var textSplitter = new TokenTextSplitter();

            vectorStore.accept(textSplitter.apply(reader.get()));

            log.info("Finished loading data to vector db");
        }

    }
}
