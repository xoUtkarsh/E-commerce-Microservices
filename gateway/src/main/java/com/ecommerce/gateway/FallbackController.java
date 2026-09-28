package com.ecommerce.gateway;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public ResponseEntity<List<String>> productsFallback(){
        return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Product list is unavailable, try after some time"));
    }
}
