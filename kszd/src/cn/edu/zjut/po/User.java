package cn.edu.zjut.po;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 13259
 */
public class User implements java.io.Serializable{
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Ledger> getLedgers() {
        return ledgers;
    }

    public void setLedgers(Set<Ledger> ledgers) {
        this.ledgers = ledgers;
    }
    public void addLedgers(Ledger ledger) {
        ledgers.add(ledger);
    }

    private String email;
    private String name;
    private String password;
    private Set<Ledger> ledgers = new HashSet<>();

    public User() {}
    public User(String email) {
        this.email = email;
    }
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}