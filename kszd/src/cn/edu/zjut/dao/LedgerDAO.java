package cn.edu.zjut.dao;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Ledger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * @author 13259
 */
public class LedgerDAO {
    private Log log = LogFactory.getLog(Ledger.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 根据hql查找账本
     * @param hql
     */
    public Ledger getLedger(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (Ledger) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 查找该用户的所有账本
     * @param email
     */
    public List getAllLedger(String email) {
        log.debug("finding all Ledger instances");
        try {
            String queryString = "from Ledger l where l.user.email="+"'"+email+"'";
            Query queryObject = session.createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 新建账本
     * @param loginuser
     */
    public void saveledger(Ledger loginuser) {
        log.debug("saving ledger instance");
        try {
            session.save(loginuser);
            log.debug("save ledger successful");
        } catch (RuntimeException re) {
            log.error("save ledger failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 删除该账本
     * @param ledgerId
     */
    public void delete(int ledgerId) {
        log.debug("delete ledger instance");
        try {
            // 根据账单ID获取账单对象
            Ledger ledger = session.get(Ledger.class, ledgerId);
            // 删除账本
            if (ledger != null) {
                session.delete(ledger);
            } else {
                System.out.println("Ledger not found with id: " + ledgerId);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 判断该账本是否超支
     * @param ledger
     */
    public boolean isBudgetExceedance(Ledger ledger) {
        // 判断是否超支
        if (ledger.getCurrent_budget() < ledger.getBudget() * 0.1) {
            return true;
        }
        return false;
    }

}
