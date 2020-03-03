package com.example.rebunu;

/**
 * This is the QRCode class implementation.
 * @author Bofeng Chen
 */
public class QRCode {
    private Integer width;
    private Integer height;
    private String content;

    /**
     * Constructor for Rating
     * @param width a positive Integer
     * @param height a positive Integer
     * @param content a Sting
     * @throws Exception null or empty string
     */
    public QRCode(int width, int height, String content) throws Exception {
        setWidth(width);
        setHeight(height);
        setContent(content);
    }

    /**
     * Getter for width
     * @return width
     */
    public Integer getWidth(){
        return width;
    }

    /**
     * Getter for height
     * @return height
     */
    public Integer getHeight(){
        return height;
    }

    /**
     * Getter for content
     * @return content
     */
    public String getContent(){
        return content;
    }

    /**
     * Setter for width
     * @param width a positive Integer
     * @throws Exception invalid number
     */
    public void setWidth(Integer width)throws Exception{
        if (width >= 0){
            this.width = width;
        } else {
            throw new Exception("Invalid width.");
        }
    }


    /**
     * Setter for height
     * @param height a positive Integer
     * @throws Exception invalid number
     */
    public void setHeight(Integer height)throws Exception{
        if (height >= 0){
            this.height = height;
        } else {
            throw new Exception("Invalid height.");
        }
    }

    /**
     * Setter for content
     * @param content a String
     * @throws NullPointerException null exception or empty exception
     */
    public void setContent(String content)throws Exception{
        if(content == null) {
            throw new NullPointerException("Content is null.");
        } else {
            if(content.isEmpty()) {
                throw new Exception("Role is empty");
            } else {
                this.content = content;
            }
        }
    }
}
