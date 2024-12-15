package com.shr4pnel.web;

import com.google.common.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.UUID;

@SpringBootApplication
@RestController
public class WebApplication {
    private static final Logger webApplicationLogger = LogManager.getLogger(WebApplication.class);
    private final LocalMiddleFactory middleFactory = new LocalMiddleFactory();

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
            sr = middleFactory.makeStockReader();
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
    @PostMapping("/api/staff/buy")
    public ResponseEntity<?> buyStock(HttpEntity<String> httpEntity) {
        webApplicationLogger.trace("POST /api/staff/buy");
        String json = httpEntity.getBody();
        BuyStockHelper stockToBuy = new Gson().fromJson(json, BuyStockHelper.class);
        StockRW srw;
        if (stockToBuy.quantity > 99 || stockToBuy.quantity < 0)
            return new ResponseEntity<>("you make me sick!", HttpStatus.FORBIDDEN);
        boolean success = false;
        try {
            srw = (StockRW) middleFactory.makeStockReadWriter();
            srw.addStock(stockToBuy.pNum, stockToBuy.quantity);
            webApplicationLogger.debug("Ordering {} of {}", stockToBuy.quantity, stockToBuy.pNum);
            success = true;
        } catch (StockException e) {
            webApplicationLogger.error(
                    "Error occurred while attempting to instantiate Stock Read-Writer", e);
        }
        if (success) return new ResponseEntity<>(null, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/customer/buy")
    public ResponseEntity<?> buyStockCustomer(HttpEntity<String> httpEntity) {
        webApplicationLogger.trace("POST /api/customer/buy");
        String json = httpEntity.getBody();
        Gson gson = new Gson();

        // https://google.github.io/gson/UserGuide.html#collections-examples
        // what a doozy!
        Type buyStockResponse = new TypeToken<Collection<BuyStockHelper>>() {}.getType();
        Collection<BuyStockHelper> jsonArray = gson.fromJson(json, buyStockResponse);
        StockRW srw;
        UUID orderuuid = UUID.randomUUID();
        assert jsonArray != null;

        try {
            srw = middleFactory.makeStockReadWriter();
            srw.addOrder(orderuuid);
        } catch (StockException e) {
            webApplicationLogger.error("Failed to create entry in ordertable", e);
        }

        for (BuyStockHelper bsh : jsonArray) {
            try {
                srw = middleFactory.makeStockReadWriter();
                srw.addBasket(orderuuid, bsh.pNum, bsh.quantity);
            } catch (StockException e) {
                webApplicationLogger.error(
                        "Failed to instantiate stock read-writer while executing customer purchase.",
                        e);
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/staff/pack")
    public ResponseEntity<?> getOrdersToPack() {
        String jsonResponse = null;
        try {
            StockR sr = middleFactory.makeStockReader();
            jsonResponse = sr.getAllOrdersToPack();
        } catch (StockException e) {
            webApplicationLogger.error("Failed to create stockreader in packing manager", e);
        }
        if (jsonResponse == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
}
