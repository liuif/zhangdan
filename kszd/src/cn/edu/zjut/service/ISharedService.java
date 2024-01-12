package cn.edu.zjut.service;

import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.Shared;

import java.util.List;

public interface ISharedService {
    public boolean removeOneLedger(int id);
    public boolean joinLedger(Shared shared);
    public boolean updateSharedLedger(Shared shared);
    public List<Ledger> getAllLedger(String email);
    public Shared getSharedbyid(int ledger_id,String email);
    public Shared getShared(int id);
    public boolean checkIfShared(int ledger_id,String email);
    public boolean checkIfSharedExists(int ledger_id);

    public List<Object[]> getApplicant(String email);
}
