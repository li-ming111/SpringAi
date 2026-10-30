package com.zmx.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @GetMapping("/text")
    public List<Document> readText() {
        //读取文件的资源
        Resource resource = new DefaultResourceLoader().getResource("classpath:data/text.txt");
        //读取文件的数据
        TextReader textReader = new TextReader(resource);
        return textReader.read();
    }
    @GetMapping("/json")
    public List<Document> readJson() {
        //读取文件的资源
        Resource resource = new DefaultResourceLoader().getResource("classpath:data/text.json");
        //读取文件的数据
        JsonReader jsonReader = new JsonReader(resource);
        return jsonReader.read();
    }
    @GetMapping("/pdf")
    public List<Document> readPdf() {
        //读取文件的资源
        Resource resource = new DefaultResourceLoader().getResource("classpath:data/课件.pdf");
        //读取文件的数据
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource);
        return pagePdfDocumentReader.read();
    }
    @GetMapping("/markdown")
    public List<Document> readMarkdown() {
        //读取文件的数据
        MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader("classpath:data/text.md");
        return markdownDocumentReader.read();
    }
    @GetMapping("/html")
    public List<Document> readHtml() {
        //读取文件的资源
        Resource resource = new DefaultResourceLoader().getResource("classpath:data/spring-ai.html");
        //读取文件的数据;只允许读取html格式
        JsoupDocumentReader jsoupDocumentReader = new JsoupDocumentReader(resource);
        return jsoupDocumentReader.read();
    }

    @GetMapping("/tika")
    public List<Document> readTika() {
        //读取文件的资源
        Resource resource = new DefaultResourceLoader().getResource("classpath:data/阿里云百炼.html");
        //读取文件的数据;允许读取各种格式(多种文档格式)
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        return tikaDocumentReader.read();
    }
}




























