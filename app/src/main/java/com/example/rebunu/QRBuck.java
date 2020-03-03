package com.example.rebunu;

/**
 * This is the QRBuck class implementation.
 * @author Bofeng Chen
 */
public class QRBuck {
    private Integer amount;

    /**
     * Constructor for QRBuck
     * @param amount a integer
     * @throws Exception null or empty value or invalid number exceptions
     * @see QRCode
     */
    public QRBuck(Integer amount)
        throws Exception{
        setAmount(amount);
    }

    /**
     * Getter for amount
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Setter for amount
     * @param amount a positive integer
     * @throws NullPointerException null exception or invalid exceptions
     */
    public void setAmount(Integer amount) throws Exception {
        if (amount == null) {
            throw new NullPointerException("driveId is null.");
        } else {
            if (amount < 0) {
                throw new Exception("Invalid amount.");
            } else {
                this.amount = amount;
            }
        }

    }
}
