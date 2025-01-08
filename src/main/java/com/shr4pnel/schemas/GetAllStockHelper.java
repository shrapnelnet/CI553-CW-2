package com.shr4pnel.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A schema, used to serialize and deserialize JSON when querying the StockTable. used in the /api/stock endpoint call.
 *
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

    /**
     * <p>Constructor for GetAllStockHelper.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param price a long
     * @param stockLevel a long
     * @param pNum a {@link java.lang.String} object
     */
    public GetAllStockHelper(String name, long price, long stockLevel, String pNum) {
        this.name = name;
        this.price = price;
        this.stockLevel = stockLevel;
        this.pNum = pNum;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>price</code>.</p>
     *
     * @return a long
     */
    public long getPrice() {
        return price;
    }

    /**
     * <p>Setter for the field <code>price</code>.</p>
     *
     * @param price a long
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * <p>Getter for the field <code>stockLevel</code>.</p>
     *
     * @return a long
     */
    public long getStockLevel() {
        return stockLevel;
    }

    /**
     * <p>Setter for the field <code>stockLevel</code>.</p>
     *
     * @param stockLevel a long
     */
    public void setStockLevel(long stockLevel) {
        this.stockLevel = stockLevel;
    }

    /**
     * <p>Getter for the field <code>pNum</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getpNum() {
        return pNum;
    }

    /**
     * <p>Setter for the field <code>pNum</code>.</p>
     *
     * @param pNum a {@link java.lang.String} object
     */
    public void setpNum(String pNum) {
        this.pNum = pNum;
    }
}
