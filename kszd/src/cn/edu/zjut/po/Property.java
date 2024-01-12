package cn.edu.zjut.po;

/**
 * @author 13259
 */
public class Property {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    int property_id;

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    String property_name;

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    double property_balance;

    public double getProperty_balance() {
        return property_balance;
    }

    public void setProperty_balance(double property_balance) {
        this.property_balance = property_balance;
    }
}
