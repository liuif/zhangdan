package cn.edu.zjut.po;

/**
 * @author 13259
 */
public class Shared {
    private int id;
    private Ledger ledger;
    private User user;
    private int allowed;

    public int getAllowed() {
        return allowed;
    }

    public void setAllowed(int allowed) {
        this.allowed = allowed;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public Shared(){}
}