package com.shr4pnel.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A schema, used to serialize and deserialize JSON when querying the StockTable. used in the /api/stock endpoint call.
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 */
@Schema(name = "Get all stock schema")
public class GetAllStockHelper {
    @Schema(name = "Item name", description = "Name of item", example = "32GB USB Drive")
    private String name;

    @Schema(name = "Item price", example = "7")
    private long price;

    @Schema(name = "Quantity in stock", example = "4")
    private long stockLevel;

    @Schema(name = "Product number", example = "0001")
    private String pNum;

    public GetAllStockHelper(String name, long price, long stockLevel, String pNum) {
        this.name = name;
        this.price = price;
        this.stockLevel = stockLevel;
        this.pNum = pNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(long stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }
}
