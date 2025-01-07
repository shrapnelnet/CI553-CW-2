package com.shr4pnel.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A schema, used to serialize JSON. This serializes GET requests which are used by the backdoor client when adding new items to the database.
 * @since v0.1.0
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 */
@Schema(name = "Customer product purchase schema")
public class BuyStockHelper {
    @Schema(name = "Product number", example = "0002")
    public String pNum;

    @Schema(name = "Quantity", example = "4")
    public int quantity;

    public BuyStockHelper(String pNum, int quantity) {
        this.pNum = pNum;
        this.quantity = quantity;
    }

    public String getpNum() {
        return this.pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
