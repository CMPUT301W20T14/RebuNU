package com.example.rebunu;

/**
 * This is the Rating class implementation
 * @author Zijian Xi
 * @see Profile
 */
public class Rating {
    private Integer thumbsUp;
    private Integer thumbsDown;

    /**
     * Constructor for Rating
     * @param thumbsUp a positive Integer
     * @param thumbsDown a positive Integer
     * @throws Exception null or invalid value exceptions
     * @see Profile
     */
    Rating(Integer thumbsUp, Integer thumbsDown) throws Exception {
        setThumbsUp(thumbsUp);
        setThumbsDown(thumbsDown);
    }
    /**
     * getter for thumbsDown
     * @return thumbsDown
     */
    public Integer getThumbsDown() {
        return thumbsDown;
    }

    /**
     * getter for thumbsUp
     * @return thumbsUp
     */
    public Integer getThumbsUp() {
        return thumbsUp;
    }

    /**
     * setter for thumbsDown
     * @param thumbsDown a positive Integer
     * @throws Exception a positive Integer
     */
    void setThumbsDown(Integer thumbsDown) throws Exception{
        if(thumbsDown == null) {
            throw new NullPointerException("ThumbsDown is null.");
        } else {
            if (thumbsDown >= 0) {
                this.thumbsDown = thumbsDown;
            } else {
                throw new Exception("Invalid thumbsDown");
            }
        }
    }

    /**
     * setter for thumbsUp
     * @param thumbsUp a positive Integer
     * @throws Exception a positive Integer
     */
    void setThumbsUp(Integer thumbsUp) throws Exception {
        if(thumbsUp == null) {
            throw new NullPointerException("ThumbsUp is null.");
        } else {
            if (thumbsUp >= 0){
                this.thumbsUp = thumbsUp;
            } else {
                throw new Exception("Invalid thumbsUp.");
            }
        }
    }

    /**
     * setter for making thumbsUp + 1
     */
    public void thumbsUpPlus(){
        this.thumbsUp += 1;
    }

    /**
     * setter for making thumbsDown + 1
     */
    public void thumbsDownPlus() {
        this.thumbsDown += 1;
    }
}
