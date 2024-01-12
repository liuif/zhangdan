package cn.edu.zjut.service;

import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Ledger;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IBudgetService {
    public boolean updateTotalBudget(Ledger ledger,List list);
    public boolean updateBudgetDetails(Budget budget);
    public List<Object[]> getTotalBudget(int ledgerid, LocalDate date);

    boolean removeBudget(int ledger_id, int type_id, LocalDate period);
    public List getBudget(Ledger ledger, LocalDate date);
}
