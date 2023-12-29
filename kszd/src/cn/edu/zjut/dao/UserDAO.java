package cn.edu.zjut.dao;

import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserDAO {
    private Log log = LogFactory.getLog(UserDAO.class);
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    public String getpassword(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return queryObject.getSingleResult().toString();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    public void save(User loginuser) {
        log.debug("saving user instance");
        try {
            session.save(loginuser);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally {
        }
    }

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

}