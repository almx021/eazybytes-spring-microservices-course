package com.mendes.gatewayserver.controller;

import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class FallbackController {
    
    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error ocurred. Please try after some time or contact support team!");
    }
    
}
