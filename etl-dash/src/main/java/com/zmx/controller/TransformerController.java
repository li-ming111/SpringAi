package com.zmx.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/transformer")
public class TransformerController {

    private final List<Document> documents;
    private final ChatModel chatModel;

    // 构造函数
    public TransformerController(ChatModel chatModel) {
        //读取pdf文档
        Resource resource = new DefaultResourceLoader().getResource("classpath:/data/课件.pdf");
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource);
        this.documents = pagePdfDocumentReader.read();
        //注入模型
        this.chatModel = chatModel;
    }

    //读取文件后分隔 文档
    @GetMapping("/token-text-splitter")
    public List<Document> tokenTextSplitter() {
        //创建分词器
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(1000)//每个文本块的token数量
                .withMinChunkSizeChars(350)//每个文本块最小的字符数
                .withMinChunkLengthToEmbed(5)//丢失小于5个字符的文本块
                .withMaxNumChunks(10000)//文本中生成的最大文本块数量
                .withKeepSeparator(true)//是否保留分隔符
                .build();
        //对文档进行分词
        List<Document> documents = tokenTextSplitter.split(this.documents);
        return documents;
    }
}






























