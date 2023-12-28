package cn.edu.zjut.service;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.ExpenseType;

import java.time.LocalDate;
import java.util.List;

public interface IBillService {
    public boolean addOneBill(Bill bill);
    public boolean removeOneBill(Bill bill);
    public boolean updateOneBill(Bill bill);
    public boolean removeBills(List<Bill> bills);
    public List<Object[]> selectByDate(LocalDate date,String email);
    public List<Object[]> selectByType(String type,String email);
    public boolean removeExpenseType(int type);
    public boolean addExpenseType(ExpenseType type);
    public List getAllExpenseType();
    public int findtypeidbyname(String typeName);
}
