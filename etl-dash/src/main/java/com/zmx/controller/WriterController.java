package com.zmx.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/writer")
public class WriterController {

    private final List<Document> documents;
    private final SimpleVectorStore simpleVectorStore;

    // 构造函数
    public WriterController(EmbeddingModel embeddingModel) {
        //读取pdf文档
        Resource resource = new DefaultResourceLoader().getResource("classpath:/data/课件.pdf");
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource);
        this.documents = pagePdfDocumentReader.read();
        //注入模型
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    // 写入文档
    @GetMapping("/write")
    public void write() {
        String filePath = "output.txt";
        FileDocumentWriter fileDocumentWriter = new FileDocumentWriter(filePath, true);
        fileDocumentWriter.accept(this.documents);
    }

    // 存储向量
    @GetMapping("/vector")
    public void vector() {
        this.simpleVectorStore.add(this.documents);
    }

    // 查询向量
    @GetMapping("/query")
    public List<Document> query() {
        return this.simpleVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("spring")
                        .topK(5)
                        .build());
    }
}




























