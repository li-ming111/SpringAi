package com.zmx.controller;

import com.zmx.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    //add
    @GetMapping("/rag/add")
    public void addRag() {
         ragService.importDocuments();
    }
    //search
    @GetMapping("/rag/search")
    public Flux<String> searchRag(String message) {
        return ragService.retrieve( message)
                .map(r->r.getResult().getOutput().getText());
    }
}


