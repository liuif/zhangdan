package cn.edu.zjut.service;

import cn.edu.zjut.po.Ledger;

import java.util.List;

public interface ILedgerService {
    public Ledger getLedger(int ledger_id);
    public List<Ledger> getAllLedger(String user_id);
    public Ledger getLedgerbyname(String name,String email);
    public boolean removeOneLedger(int id);
    public Ledger getLedgerbyinvitation(String invitation);
    public boolean notifyBudgetExceedance(Ledger ledger);
}
