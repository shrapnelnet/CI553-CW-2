package com.shr4pnel.gsonhelpers;

import java.util.Date;

public class PackingHelper {
    private Date date;
    private String pNum;
    private int quantity;
    private String UUID;

    public PackingHelper(Date date, String pNum, int quantity, String UUID) {
        this.date = date;
        this.pNum = pNum;
        this.quantity = quantity;
        this.UUID = UUID;
    }
}
