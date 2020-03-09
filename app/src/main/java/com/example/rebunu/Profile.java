package com.example.rebunu;

/**
 * This is the Profile class implementation.
 * @author Zijian Xi
 */
public class Profile extends Record{
    private String phone;
    private String email;
    private String name;
    private Double balance;
    private Boolean role;
    private Rating rating;

    /**
     * Constructor for Profile
     * @param phone a String, but only consists of numbers
     * @param email a String
     * @param username a String
     * @param balance a Integer, non-negative
     * @param role a String
     * @param rating a Rating object
     * @throws Exception null or empty value or invalid number exceptions
     * @see Rating
     */
    public Profile(
            String phone, String email, String name, Integer balance, String role, Rating rating
    ) throws Exception{
        setPhone(phone);
        setEmail(email);
        setName(name);
        setBalance(balance);
        setRole(role);
        setRating(rating);
        setType(1);
    }

    public Profile(String phone, String email, String name, Integer balance, String role, Rating rating, Boolean noId) throws Exception {
        super(noId);
        setPhone(phone);
        setEmail(email);
        setName(name);
        setBalance(balance);
        setRole(role);
        setRating(rating);
        setType(1);
    }

    /**
     * Getter for phone
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for balance
     * @return balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * Getter for role
     * @return role
     */
    public Boolean getRole() {
        return role;
    }

    /**
     * Getter for rating
     * @return a Rating object
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Setter for phone
     * @param phone a String but only consists of numbers
     * @throws Exception null or empty or invalid number exceptions
     */
    public void setPhone(String phone) throws  Exception{
        if(phone == null) {
            throw new NullPointerException("Phone is null.");
        } else {
            if (phone.isEmpty()) {
                throw new Exception("Phone number is empty.");
            } else {
                if(phone.matches("[0-9]+")) {
                    this.phone = phone;
                } else {
                    throw new Exception(phone + "is not a valid phone number.");
                }
            }
        }
    }

    /**
     * Setter for email
     * @param email a String
     * @throws Exception null or empty exceptions
     */
    public void setEmail(String email) throws Exception{
        if(email == null) {
            throw new NullPointerException("Email is null.");
        } else {
            if(email.isEmpty()) {
                throw new Exception("Email is empty.");
            } else {
                if(email.matches("^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$")) {
                    this.email = email;
                } else {
                    throw new Exception(email + "is not a valid email address");
                }
            }
        }
    }

    /**
     * Setter for name
     * @param name a String
     * @throws Exception null or empty exceptions
     */
    public void setName(String name) throws Exception {
        if(name == null) {
            throw new NullPointerException("name is null.");
        } else {
            if(name.isEmpty()) {
                throw new Exception("name is empty.");
            } else {
                this.name = name;
            }
        }
    }

    /**
     * Setter for balance
     * @param balance a Integer
     * @throws Exception null or invalid value exceptions
     */
    public void setBalance(Integer balance) throws Exception{
        if(balance == null) {
            throw new NullPointerException("Balance is null.");
        } else {
            if(balance < 0) {
                throw new Exception("Invalid balance.");
            } else {
                this.balance = balance;
            }
        }

    }

    /**
     * Setter for role
     * @param role a String
     * @throws Exception null or empty exceptions
     */
    public void setRole(Boolean role) throws Exception{
        if(role == null) {
            throw new NullPointerException("Role is null.");
        } else {
            this.role = role;
//            if(role.isEmpty()) {
//                throw new Exception("Role is empty");
//            } else {
//                this.role = role;
//            }
        }
    }

    /**
     * Setter for rating
     * @param rating a Rating object
     * @throws NullPointerException null exception
     */
    public void setRating(Rating rating) throws NullPointerException{
        if(rating != null) {
            this.rating = rating;
            return;
        }
        throw new NullPointerException("Rating is null.");
    }
}
