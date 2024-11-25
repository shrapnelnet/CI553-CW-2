package com.shr4pnel.web;

import com.google.gson.Gson;
import com.shr4pnel.db.StockR;
import com.shr4pnel.db.StockRW;
import com.shr4pnel.gsonhelpers.BuyStockHelper;
import com.shr4pnel.middleware.LocalMiddleFactory;
import com.shr4pnel.middleware.StockException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class WebApplication {
    private static final Logger webApplicationLogger = LogManager.getLogger(WebApplication.class);
    private LocalMiddleFactory middleFactory = new LocalMiddleFactory();

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

//    @GetMapping("/")
//    public ResponseEntity<Resource> root() {
//        Resource index = new ClassPathResource("/static/index.html");
//        return ResponseEntity.ok()
//                .body(index);
//    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/stock")
    public ResponseEntity<?> getStock() {
        webApplicationLogger.trace("GET /api/stock");
        StockR sr;
        try {
            sr = (StockR) middleFactory.makeStockReader();
        } catch (StockException e) {
            webApplicationLogger.error("Endpoint /api/stock failed to instantiate StockReader", e);
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        String json = sr.getAllStock();
        if (json == null) {
            webApplicationLogger.error("Endpoint /api/stock failed to retrieve stock levels");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/buy")
    public ResponseEntity<?> buyStock(HttpEntity<String> httpEntity) {
        webApplicationLogger.trace("POST /api/buy");
        String json = httpEntity.getBody();
        BuyStockHelper stockToBuy = new Gson().fromJson(json, BuyStockHelper.class);
        StockRW srw;
        boolean success = false;
        try {
            srw = (StockRW) middleFactory.makeStockReadWriter();
            srw.addStock(stockToBuy.pNum, stockToBuy.quantity);
            webApplicationLogger.debug("Ordering {} of {}", stockToBuy.pNum, stockToBuy.quantity);
            success = true;
        } catch (StockException e) {
            webApplicationLogger.error("Error occurred while attempting to instantiate Stock Read-Writer", e);
        }
        if (success)
            return new ResponseEntity<>(null, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
