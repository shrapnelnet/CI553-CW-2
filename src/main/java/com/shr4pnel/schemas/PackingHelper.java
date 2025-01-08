package com.shr4pnel.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * A schema, used to serialize JSON by the API. used in the /api/staff/pack endpoint.
 *
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 */
@Schema(name = "Get all unpacked items schema")
public class PackingHelper {
    @Schema(name = "Purchase date", description = "The date the cashier client processed the purchase, without timestamp.", example = "Jan 1 2025")
    private Date date;

    @Schema(name ="Product number", example = "0001")
    private String pNum;

    @Schema(name="Quantity", example = "4")
    private int quantity;

    @Schema(name = "UUID", description = "Unique identifier for each order", example = "8cab1365-11f4-49a2-b710-fcb5bb2afbc4")
    private String UUID;

    /**
     * <p>Constructor for PackingHelper.</p>
     *
     * @param date a {@link java.util.Date} object
     * @param pNum a {@link java.lang.String} object
     * @param quantity a int
     * @param UUID a {@link java.lang.String} object
     */
    public PackingHelper(Date date, String pNum, int quantity, String UUID) {
        this.date = date;
        this.pNum = pNum;
        this.quantity = quantity;
        this.UUID = UUID;
    }

    /**
     * <p>Getter for the field <code>date</code>.</p>
     *
     * @return a {@link java.util.Date} object
     */
    public Date getDate() {
        return date;
    }

    /**
     * <p>Setter for the field <code>date</code>.</p>
     *
     * @param date a {@link java.util.Date} object
     */
    public void setDate(Date date) {
        this.date = date;
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

    /**
     * <p>Getter for the field <code>quantity</code>.</p>
     *
     * @return a int
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * <p>Setter for the field <code>quantity</code>.</p>
     *
     * @param quantity a int
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * <p>getUUID.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * <p>setUUID.</p>
     *
     * @param UUID a {@link java.lang.String} object
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
