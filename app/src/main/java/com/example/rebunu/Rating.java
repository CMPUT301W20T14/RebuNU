package com.example.rebunu;

public class Rating {
    private Integer thumbsUp;
    private Integer thumbsDown;

    public Integer getThumbsDown() {
        return thumbsDown;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    void setThumbsDown(Integer thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public void thumbsUpPlus(){
        this.thumbsUp += 1;
    }

    public void thumbsDownPlus() {
        this.thumbsDown += 1;
    }
}
