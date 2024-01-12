package cn.edu.zjut.po;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 13259
 */
public class Bill implements Serializable{
    private int billId;
    private Ledger ledger;
    private Double money;
    private UserExpenseType userexpensetype;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String incomeOrExpense;
    private String note;
    private LocalDateTime billCreateDate;

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public void setBillCreateDate(LocalDateTime billCreateDate) {
        this.billCreateDate = billCreateDate;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setIncomeOrExpense(String incomeOrExpense) {
        this.incomeOrExpense = incomeOrExpense;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setUserexpensetype(UserExpenseType userexpensetype) {
        this.userexpensetype = userexpensetype;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public Double getMoney() {
        return money;
    }

    public int getBillId() {
        return billId;
    }

    public LocalDateTime getBillCreateDate() {
        return billCreateDate;
    }

    public String getIncomeOrExpense() {
        return incomeOrExpense;
    }

    public String getNote() {
        return note;
    }

    public UserExpenseType getUserexpensetype() {
        return userexpensetype;
    }

    public Bill() {}
    public Bill(Ledger ledger, Double money, UserExpenseType userexpensetype,
                String incomeOrExpense, String note, LocalDateTime billCreateDate,User user) {
        this.ledger = ledger;
        this.money = money;
        this.userexpensetype = userexpensetype;
        this.incomeOrExpense = incomeOrExpense;
        this.note = note;
        this.billCreateDate = billCreateDate;
        this.user=user;
    }
}