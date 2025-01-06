package com.shr4pnel.web;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.shr4pnel.db.StockR;
import com.shr4pnel.db.StockRW;
import com.shr4pnel.middleware.LocalMiddleFactory;
import com.shr4pnel.middleware.StockException;
import com.shr4pnel.schemas.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(title = "Catshop REST API", version = "v1.0.2", description = "Entrypoint for Spring-boot. Handles communication between the client and database, as well as hosting the frontend itself.", license = @License(name = "GNU GPLv3", url = "https://www.gnu.org/licenses/gpl-3.0.en.html"), contact = @Contact(name = "Tyler", email = "tyler@shrapnelnet.co.uk")))
@SpringBootApplication
@RestController
public class WebApplication {
    private static final Logger webApplicationLogger = LogManager.getLogger(WebApplication.class);
    private final LocalMiddleFactory middleFactory = new LocalMiddleFactory();

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Hidden
    @GetMapping("/")
    public ResponseEntity<Resource> root() {
        Resource index = new ClassPathResource("/index.html");
        return ResponseEntity.ok().body(index);
    }

    @Operation(summary = "Get stock level", description = "Get the current amount of stock in the database, and send it to the client. Used to synchronize customer, cashier and backdoor clients.", responses = {@ApiResponse(content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetAllStockHelper.class), minItems = 0), examples = {@ExampleObject(name = "Stock", description = "Get stock count", value = "[{\"name\":\"40 inch LED HD TV\",\"price\":269,\"stockLevel\":90,\"pNum\":\"0001\"},{\"name\":\"DAB Radio\",\"price\":29,\"stockLevel\":20,\"pNum\":\"0002\"},{\"name\":\"Toaster\",\"price\":19,\"stockLevel\":33,\"pNum\":\"0003\"},{\"name\":\"Watch\",\"price\":29,\"stockLevel\":10,\"pNum\":\"0004\"},{\"name\":\"Digital Camera\",\"price\":89,\"stockLevel\":17,\"pNum\":\"0005\"},{\"name\":\"MP3 player\",\"price\":7,\"stockLevel\":15,\"pNum\":\"0006\"},{\"name\":\"32Gb USB2 drive\",\"price\":6,\"stockLevel\":1,\"pNum\":\"0007\"}]"),}), description = "JSON object populated with product names, stock levels and product numbers digested by the client to display stock levels", responseCode = "200"), @ApiResponse(responseCode = "500", description = "This endpoint will return a HTTP 500 error if a connection to the database cannot be made for any reason.", content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping("/api/stock")
    public ResponseEntity<?> getStock() {
        webApplicationLogger.trace("GET /api/stock");
        StockR sr;
        String json;
        try {
            sr = middleFactory.makeStockReader();
            json = sr.getAllStock();
        } catch (Exception e) {
            webApplicationLogger.error("Endpoint /api/stock failed to instantiate StockReader", e);
            return ResponseEntity.internalServerError().build();
        }
        if (json == null) {
            webApplicationLogger.error("Endpoint /api/stock failed to retrieve stock levels");
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().header("Content-Type", "application/json").body(json);
    }

    @Operation(summary = "Buy new stock", description = "Adds specified amount of stock to the database", responses = {@ApiResponse(responseCode = "204", description = "Successful addition of new stock to database", content = @Content(schema = @Schema(hidden = true))), @ApiResponse(responseCode = "403", description = "Attempt to add less than zero or more than 99 items at a time", content = @Content(schema = @Schema(hidden = true))), @ApiResponse(responseCode = "500", description = "Server cannot connect to database", content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/api/staff/buy")
    public ResponseEntity<?> buyStock(@RequestBody(description = "JSON object containing product number and quantity of product to buy", content = @Content(examples = @ExampleObject(name = "Request body", description = "Stringified JSON body with product number and quantity wanted", value = "{ \"quantity\": \"5\", \"pNum\": \"0003\" }"), schema = @Schema(implementation = BuyStockHelper.class))) HttpEntity<String> httpEntity) {
        webApplicationLogger.trace("POST /api/staff/buy");
        String json = httpEntity.getBody();
        BuyStockHelper stockToBuy = new Gson().fromJson(json, BuyStockHelper.class);
        StockRW srw;
        if (stockToBuy.quantity > 99 || stockToBuy.quantity < 0)
            return ResponseEntity.status(403).body("you make me sick!");
        boolean success = false;
        try {
            srw = middleFactory.makeStockReadWriter();
            srw.addStock(stockToBuy.pNum, stockToBuy.quantity);
            webApplicationLogger.debug("Ordering {} of {}", stockToBuy.quantity, stockToBuy.pNum);
            success = true;
        } catch (StockException e) {
            webApplicationLogger.error("Error occurred while attempting to instantiate Stock Read-Writer", e);
        }
        if (success) return ResponseEntity.noContent().build();
        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Buy products", description = "Removes stock from the StockTable, and places it into the OrderTable and BasketTable respectively", responses = {@ApiResponse(responseCode = "204", description = "Empty response, signifies successful purchase on clientside", content = @Content(schema = @Schema(hidden = true))), @ApiResponse(responseCode = "500", description = "Server cannot connect to database", content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/api/customer/buy")
    public ResponseEntity<?> buyStockCustomer(@RequestBody(description = "JSON Object containing a basket, an array of objects, each of which have a product number and a quantity to purchase.", content = @Content(examples = @ExampleObject(name = "Request body", description = "Stringified JSON body with product number and quantity wanted", value = "[{\"name\":\"DAB Radio\",\"quantity\":4,\"pNum\":\"0002\",\"price\":29},{\"name\":\"Digital Camera\",\"quantity\":2,\"pNum\":\"0005\",\"price\":89}]"), array = @ArraySchema(schema = @Schema(implementation = BuyStockHelper.class)))) HttpEntity<String> httpEntity) {
        webApplicationLogger.trace("POST /api/customer/buy");
        String json = httpEntity.getBody();
        Gson gson = new Gson();

        // https://google.github.io/gson/UserGuide.html#collections-examples
        // what a doozy!
        Type buyStockResponse = new TypeToken<Collection<BuyStockHelper>>() {}.getType();
        Collection<BuyStockHelper> jsonArray = gson.fromJson(json, buyStockResponse);
        StockRW srw;
        UUID orderuuid = UUID.randomUUID();
        if (jsonArray == null) {
            return ResponseEntity.internalServerError().build();
        }
        try {
            srw = middleFactory.makeStockReadWriter();
            srw.addOrder(orderuuid);
        } catch (StockException e) {
            webApplicationLogger.error("Failed to create entry in ordertable", e);
            return ResponseEntity.internalServerError().build();
        }
        for (BuyStockHelper bsh : jsonArray) {
            srw.addBasket(orderuuid, bsh.pNum, bsh.quantity);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all items that are waiting to be packed", description = "Fetch all items in OrderTable, then all items in BasketTable with a matching foreign key to aggregate the baskets correctly", responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved packing orders", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Response body", summary = "Array of orders that need to be packed", description = "Array children with identical orderIDs get merged in clientside javascript.", value = "[{\"date\":\"Jan 2, 2025\",\"pNum\":\"0002\",\"quantity\":4,\"UUID\":\"acc53edd-d266-463d-abf1-a032853c56b6\"},{\"date\":\"Jan 2, 2025\",\"pNum\":\"0005\",\"quantity\":2,\"UUID\":\"acc53edd-d266-463d-abf1-a032853c56b6\"}]"), array = @ArraySchema(schema = @Schema(implementation = PackingHelper.class))))})
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
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().header("Content-Type", "application/json").body(jsonResponse);
    }

    @Operation(summary = "Delete packed orders", description = "Delete orders that are sent to be packed in the packing frontend client.", responses = {@ApiResponse(responseCode = "204", description = "Successful deletion", content = @Content(schema = @Schema(hidden = true))),@ApiResponse(responseCode = "403", description = "Item does not exist in database", content = @Content(schema = @Schema(hidden = true))),@ApiResponse(responseCode = "500", description = "Connection to database has failed", content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping("/api/staff/finalizePack")
    public ResponseEntity<?> finishPacking(@Parameter(name = "orderid", description = "Order UUID", example = "b3d68113-2938-4763-bee9-c9276c5aa19d") @RequestParam("orderid") String orderID) {
        try {
            StockRW srw = middleFactory.makeStockReadWriter();
            boolean success = srw.packOrder(orderID);
            if (success) return ResponseEntity.noContent().build();
            return ResponseEntity.status(403).build();
        } catch (StockException e) {
            webApplicationLogger.error("Failed to instantiate stock read-writer while deleting order.", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
