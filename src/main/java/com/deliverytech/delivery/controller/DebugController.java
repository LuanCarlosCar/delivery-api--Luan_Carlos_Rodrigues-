package com.deliverytech.delivery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @GetMapping("/test")
    public Map<String, String> test() {
        logger.info("🔧 Debug endpoint acessado sem autenticação");
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Endpoint público funcionando");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }

    @GetMapping("/swagger-status")
    public Map<String, String> swaggerStatus() {
        logger.info("🔧 Swagger status endpoint acessado");
        Map<String, String> response = new HashMap<>();
        response.put("swagger", "accessible");
        response.put("security", "public");
        response.put("message", "Se você está vendo isso, os endpoints públicos estão funcionando");
        return response;
    }
}