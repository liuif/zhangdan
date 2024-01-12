package cn.edu.zjut.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import cn.edu.zjut.dao.LedgerDAO;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.util.HibernateUtil;

/**
 * @author 13259
 */
@Service("ledgerService")
public class LedgerService implements ILedgerService{
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 根据账本id获取账本
     * @param ledger_id
     * @return Ledger
     */
    public Ledger getLedger(int ledger_id){
        String hql = "FROM Ledger WHERE ledgerId = '"
                +ledger_id +"'";
        Session session=this.getSession();
        LedgerDAO dao=new LedgerDAO();
        dao.setSession(session);
        Ledger ledger=dao.getLedger(hql);
        return ledger;
    }

    /**
     * 根据账本名和用户邮箱查找账本
     * @param name,email
     * @return boolean
     */
    public Ledger getLedgerbyname(String name,String email){
        String hql = "FROM Ledger l WHERE l.ledgerName = '"
                +name +"' and l.user.email = '"+email+"'";
        Session session=this.getSession();
        LedgerDAO dao=new LedgerDAO();
        dao.setSession(session);
        Ledger ledger=dao.getLedger(hql);
        return ledger;
    }

    /**
     * 添根据邀请码查找账本
     * @param invitation
     * @return Ledger
     */
    public Ledger getLedgerbyinvitation(String invitation){
        String hql = "FROM Ledger l WHERE l.invitation = '"
                +invitation +"'";
        Session session=this.getSession();
        LedgerDAO dao=new LedgerDAO();
        dao.setSession(session);
        Ledger ledger=dao.getLedger(hql);
        return ledger;
    }

    /**
     * 获取用户的全部账本
     * @param email
     * @return List<Ledger>
     */
    public List<Ledger> getAllLedger(String email) {
        Session session=this.getSession();
        LedgerDAO dao = new LedgerDAO();
        dao.setSession(session);
        List items = dao.getAllLedger(email);
        HibernateUtil.closeSession();
        return items;
    }

    /**
     * 添加一条账本
     * @param ledger
     * @return boolean
     */
    public boolean addLedger(Ledger ledger){
        Session session=this.getSession();
        LedgerDAO dao = new LedgerDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.saveledger(ledger);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("save ledger failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 删除账本
     * @param id
     * @return boolean
     */
    public boolean removeOneLedger(int id) {
        Session session=this.getSession();
        LedgerDAO dao = new LedgerDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.delete(id);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete ledger failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 提醒超支
     * @param ledger
     * @return 超支返回1，不超支返回0
     */
    public boolean notifyBudgetExceedance(Ledger ledger) {
        Session session=this.getSession();
        LedgerDAO dao = new LedgerDAO();
        dao.setSession(session);
        if(dao.isBudgetExceedance(ledger)) {
            return true;
        }
        return false;
    }
}
