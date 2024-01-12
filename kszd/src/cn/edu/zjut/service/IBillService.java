package cn.edu.zjut.service;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.UserExpenseType;

import java.time.LocalDate;
import java.util.List;

public interface IBillService {
    public Bill getBill(int bill_id);
    public boolean addOneBill(Bill bill);
    public boolean removeOneBill(Bill bill);
    public boolean updateOneBill(Bill bill);
    public List<Object[]> selectByDate(LocalDate date,int ledgerId);
    public Double INgetTotalIncomeAndExpenseByYear(int year, String email,int month);
    public Double OUTgetTotalIncomeAndExpenseByYear(int year, String email,int month);
    public Double INgetTotalIncomeAndExpenseByMonth(int year, String email,int month,int day);
    public Double OUTgetTotalIncomeAndExpenseByMonth(int year, String email,int month,int day);
    public Double INfindDailyIncomeExpenseNetByYear(int year, int type_id, String email);
    public Double OUTfindDailyIncomeExpenseNetByYear(int year, int type_id, String email);
}
