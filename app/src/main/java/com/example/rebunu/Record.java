package com.example.rebunu;

/**
 * This is a class that as the operation unit of the database
 * @author Zihao Huang
 * @see Database
 */
public class Record {
    private String id;
    private Integer type;//1 for profile; 2 for request; 3 for order;

    /**
     * Constructor
     */
    public Record(){
    }

    public Record(Boolean noId) {}

    /**
     * get id
     * @return id
     */
    public String getId(){
        return this.id;
    }

    /**
     * set id
     * @param id
     * @throws NullPointerException
     */

    public void setId(String id){
        if(id ==  null){
            throw new NullPointerException();
        }
        this.id = id;
    }

    /**
     * get type
     * @return type
     */

    public Integer getType(){
        return this.type;
    }

    /**
     * set type
     * @param type
     * @throws IllegalArgumentException
     */

    public void setType(Integer type){
        if(!(type == 1 || type == 2 || type == 3 || type == 4 || type == 5)){
            throw new IllegalArgumentException();
        }
        this.type = type;
    }
}
