package cn.edu.zjut.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 13259
 */
public class Budget implements Serializable{
    Ledger ledger;
    private UserExpenseType userexpensetype;

    public UserExpenseType getUserexpensetype() {
        return userexpensetype;
    }

    public void setUserexpensetype(UserExpenseType userexpensetype) {
        this.userexpensetype = userexpensetype;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public UserExpenseType getuserexpensetype() {
        return userexpensetype;
    }

    public UserExpenseType getUserExpenseType() {
        return userexpensetype;
    }

    public void setuserexpensetype(UserExpenseType userexpensetype) {
        this.userexpensetype = userexpensetype;
    }

    LocalDateTime period;

    public LocalDateTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalDateTime period) {
        this.period = period;
    }

    private double budget;
    private double currentbudget;

    public double getCurrentbudget() {
        return currentbudget;
    }

    public void setCurrentbudget(double currentbudget) {
        this.currentbudget = currentbudget;
    }

    public void setCurrentBudget(double currentbudget) {
        this.currentbudget = currentbudget;
    }

    public double getCurrentBudget() {
        return currentbudget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
