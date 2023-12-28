package cn.edu.zjut.po;

import java.time.LocalDateTime;

public class Bill implements java.io.Serializable{
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public String getIncomeOrExpense() {
        return incomeOrExpense;
    }

    public void setIncomeOrExpense(String incomeOrExpense) {
        this.incomeOrExpense = incomeOrExpense;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getBillCreateDate() {
        return billCreateDate;
    }

    public void setBillCreateDate(LocalDateTime billCreateDate) {
        this.billCreateDate = billCreateDate;
    }

    private int billId;
    private Ledger ledger;
    private Float money;
    private ExpenseType expenseType;
    private String incomeOrExpense;
    private String note;
    private LocalDateTime billCreateDate;

    public Bill() {}
    public Bill(int billId, Ledger ledger, Float money, ExpenseType expenseType,
                String incomeOrExpense, String note, String billCreateDate) {
        this.billId = billId;
        this.ledger = ledger;
        this.money = money;
        this.expenseType = expenseType;
        this.incomeOrExpense = incomeOrExpense;
        this.note = note;
        this.billCreateDate = LocalDateTime.parse(billCreateDate);
    }
}