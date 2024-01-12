package cn.edu.zjut.po;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author b7
 */
public class Ledger implements java.io.Serializable{
    public int getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(int ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public LocalDateTime getLedgerCreateDate() {
        return ledgerCreateDate;
    }

    public void setLedgerCreateDate(LocalDateTime ledgerCreateDate) {
        this.ledgerCreateDate = ledgerCreateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    private int ledgerId;
    private String ledgerName;
    private LocalDateTime ledgerCreateDate;
    private User user;
    private Set<Bill> bills = new HashSet<>();

    private String invitation;
    private double budget;
    private double current_budget;

    public void setCurrent_budget(double current_budget) {
        this.current_budget = current_budget;
    }

    public double getCurrent_budget() {
        return current_budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public double getBudget() {
        return budget;
    }

    public String getInvitation() {
        return invitation;
    }

    public Ledger() {}

    public Ledger(int ledgerId, String ledgerName, String ledgerCreateDate, User user) {
        this.ledgerId = ledgerId;
        this.ledgerName = ledgerName;
        this.ledgerCreateDate = LocalDateTime.parse(ledgerCreateDate);
        this.user = user;
    }
}