package com.martishyn.dockerpractice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
public class Controller {

    @GetMapping("/{name}")
    public ResponseEntity<String> getGreeting(@PathVariable String name) {
        String body = "Hello, " +  name;
        return ResponseEntity.ok(body);
    }
}
