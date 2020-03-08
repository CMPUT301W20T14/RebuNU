package com.example.rebunu;

/**
 * This is the Profile class implementation.
 * @author Zijian Xi
 */
public class Profile extends Record{
    private String phone;
    private String email;
    private String username;
    private Double balance;
    private String role;
    private Rating rating;

    /**
     * Constructor for Profile
      * @param phone a String, but only consists of numbers
     * @param email a String
     * @param username a String
     * @param balance a Double, cannot be NaN or Inf
     * @param role a String
     * @param rating a Rating object
     * @throws Exception null or empty value or invalid number exceptions
     * @see Rating
     */
    public Profile(
            String phone, String email, String username, Double balance, String role, Rating rating
    ) throws Exception{
        setPhone(phone);
        setEmail(email);
        setUsername(username);
        setBalance(balance);
        setRole(role);
        setRating(rating);
        setType(1);
    }

    public Profile(String phone, String email, String username, Double balance, String role, Rating rating, Boolean noId) throws Exception {
        super(noId);
        setPhone(phone);
        setEmail(email);
        setUsername(username);
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
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for balance
     * @return balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Getter for role
     * @return role
     */
    public String getRole() {
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
     * Setter for username
     * @param username a String
     * @throws Exception null or empty exceptions
     */
    public void setUsername(String username) throws Exception {
        if(username == null) {
            throw new NullPointerException("Username is null.");
        } else {
            if(username.isEmpty()) {
                throw new Exception("Username is empty.");
            } else {
                this.username = username;
            }
        }
    }

    /**
     * Setter for balance
     * @param balance a Double
     * @throws Exception null or NaN or Inf exceptions
     */
    public void setBalance(Double balance) throws Exception{
        if(balance == null) {
            throw new NullPointerException("Balance is null.");
        } else {
            if(balance.isInfinite() || balance.isNaN()) {
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
    public void setRole(String role) throws Exception{
        if(role == null) {
            throw new NullPointerException("Role is null.");
        } else {
            if(role.isEmpty()) {
                throw new Exception("Role is empty");
            } else {
                this.role = role;
            }
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
