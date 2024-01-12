package cn.edu.zjut.service;

import cn.edu.zjut.dao.BillDAO;
import cn.edu.zjut.dao.LedgerDAO;
//import cn.edu.zjut.dao.PropertyDAO;
import cn.edu.zjut.dao.SharedDAO;
import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.Shared;
import cn.edu.zjut.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service("SharedService")
public class SharedService implements ISharedService{

    /**
     * 获得session
     * @return
     */
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 通过id获取共享账本
     * @param id
     * @return Shared
     */
    public Shared getShared(int id){
        String hql = "FROM Shared s WHERE s.id = '"
                +id +"'";
        Session session=this.getSession();
        SharedDAO dao=new SharedDAO();
        dao.setSession(session);
        Shared shared=dao.getShared(hql);
        return shared;
    }

    /**
     * 通过账本id和邮箱找到对应的共享账本记录
     * @param ledger_id，email
     * @return Shared
     */
    public Shared getSharedbyid(int ledger_id,String email){
        String hql = "FROM Shared s WHERE s.ledger.ledgerId = '"
                +ledger_id +"' and s.user.email= '"+email+"'";
        Session session=this.getSession();
        SharedDAO dao=new SharedDAO();
        dao.setSession(session);
        Shared shared=dao.getShared(hql);
        return shared;
    }

    /**
     * 删除共享账本
     * @param id
     * @return boolean
     */
    public boolean removeOneLedger(int id) {
        Session session=this.getSession();
        SharedDAO dao = new SharedDAO();
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
     * 加入到共享账本
     * @param shared
     * @return boolean
     */
    public boolean joinLedger(Shared shared) {
        Session session=this.getSession();
        SharedDAO dao = new SharedDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.save(shared);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("join shared_ledger failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 修改共享账本中的allowed
     * @param shared
     * @return boolean
     */
    public boolean updateSharedLedger(Shared shared) {
        Session session=this.getSession();
        SharedDAO dao = new SharedDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.updateAllowed(shared);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update shared ledger failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 获取用户在共享账本表里的所有记录
     * @param email
     * @return UserExpenseType
     */
    public List<Ledger> getAllLedger(String email) {
        Session session=this.getSession();
        SharedDAO dao = new SharedDAO();
        dao.setSession(session);
        List items = dao.findAllLedger(email);
        HibernateUtil.closeSession();
        return items;
    }

    /**
     * 获取该账本的所有共享记录
     * @param ledger_id
     * @return boolean
     */
    public boolean checkIfSharedExists(int ledger_id){
        String hql = "FROM Shared s WHERE s.ledger.ledgerId = '"
                +ledger_id +"'";
        Session session=this.getSession();
        SharedDAO dao=new SharedDAO();
        dao.setSession(session);
        return dao.checkIfSharedExists(hql);
    }

    /**
     * 判断该账本是否是共享账本
     * @param ledger_id, email
     * @return boolean
     */
    public boolean checkIfShared(int ledger_id,String email){
        String hql = "FROM Shared s WHERE s.ledger.ledgerId = '"
                +ledger_id +"' and s.user.email = '"+email+"'";
        Session session=this.getSession();
        SharedDAO dao=new SharedDAO();
        dao.setSession(session);
        return dao.checkIfSharedExists(hql);
    }

    /**
     * 获取所有申请人
     * @param email
     * @return List<Object[]>
     */
    public List<Object[]> getApplicant(String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql="select share.user.email,share.user.name,share.id,share.ledger.ledgerName "
                + " from Shared share "
                + " where share.ledger.user.email = '"+email+"'"
                + " and share.allowed = 0";
        List list = dao.findByHql(hql);
        HibernateUtil.closeSession();
        return list;
    }
}