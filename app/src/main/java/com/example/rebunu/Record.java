package com.example.rebunu;

/**
 * This is a class that as the operation unit of the database
 * @author Zihao Huang
 * @see Database
 */
public class Record {
    private Integer id;
    private Integer type;//1 for profile; 2 for request; 3 for order;
    Database db = new Database();

    public Record(){
        this.id = db.generateUniqueId();
    }

    /**
     * setters and getters
     */


    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getType(){
        return this.type;
    }

    public void setType(Integer type){
        this.type = type;
    }
}