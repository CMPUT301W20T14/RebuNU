package com.example.rebunu;

/**
 * This is the Order class implementation.
 * @author Bofeng Chen
 */
public class Order {
    private QRCode qr;
    private Rating rating;
    private Integer driveId;

    /**
     * Constructor for Order
     * @param qr      a QRcode object
     * @param rating  a Rating object
     * @param driveId a integer
     * @throws Exception null or empty value or invalid number exceptions
     * @see QRCode
     * @see Rating
     */

    public Order(
            QRcode qr, Rating rating, int driveId)
            throws Exception {
        setQRCode(qr);
        setRating(rating);
        setDriveId(driveId);
    }

    /**
     * Getter for qr
     * @return a qr object
     */

    public QRCode getQRCode() {
        return qr;
    }

    /**
     * Getter for rating
     * @return a rating object
     */

    public Rating getRating() {
        return rating;
    }

    /**
     * Getter for driveId
     * @return driveId
     */

    public Integer getDriveId() {
        return driveId;
    }

    /**
     * Setter for qr
     * @param qr a two-dimensional array
     * @throws NullPointerException null exception
     */
    public void setQRCode(QRCode qr) throws Exception {
        if (qr != null) {
            this.qr = qr;
            return;
        }
        throw new NullPointerException("qr is null.");
    }

    /**
     * Setter for rating
     * @param rating rating a Rating object
     * @throws NullPointerException null exception
     */
    public void setRating(Rating rating) throws NullPointerException {
        if (rating != null) {
            this.rating = rating;
            return;
        }
        throw new NullPointerException("Rating is null.");
    }

    /**
     * Setter for driveId
     * @param driveId a positive integer
     * @throws NullPointerException null exception
     */
    public void setDriveId(Integer driveId) throws Exception {
        if (driveId == null) {
            throw new NullPointerException("driveId is null.");
        } else {
            if (driveId < 0) {
                throw new Exception("Invalid driveId.");
            } else {
                this.driveId = driveId;
            }
        }
    }

    /**
     * Setter for pay
     */
    public void pay() { }
    }
}
