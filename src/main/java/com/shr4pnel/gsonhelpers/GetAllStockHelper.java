package com.shr4pnel.gsonhelpers;

public class GetAllStockHelper {
    private String name;
    private long price;
    private long stockLevel;
    private String pNum;

    public GetAllStockHelper(String name, long price, long stockLevel, String pNum) {
        this.name = name;
        this.price = price;
        this.stockLevel = stockLevel;
        this.pNum = pNum;
    }
}
