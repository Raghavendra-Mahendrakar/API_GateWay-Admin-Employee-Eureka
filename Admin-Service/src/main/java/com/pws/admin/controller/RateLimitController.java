package com.pws.admin.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/api")
public class RateLimitController {


    Refill refill = Refill.of(5, Duration.ofMinutes(1));
    private Bucket bucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();

    @GetMapping("/consume_token")
    public ResponseEntity<String> consumeToken(){

        if(bucket.tryConsume(1)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<String>("HITS EXCEEDED", HttpStatus.TOO_MANY_REQUESTS);
    }
}
