package cn.edu.zjut.dao;

import cn.edu.zjut.po.Bill;
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

    /**
     * 查找该用户
     * @param hql
     */
    public User getUser(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (User)queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 添加一条用户记录
     * @param loginuser
     */
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

    /**
     * 添加用户默认账本
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
     * 更新该用户
     * @param updatedUser
     */
    public void update(User updatedUser) {
        try {
            System.out.println(updatedUser);
            // 根据账单ID获取原始账单对象
            User existingUser = session.get(User.class, updatedUser.getEmail());
            System.out.println(existingUser);
            // 更新账单
            if (existingUser != null) {
                existingUser.setPassword(updatedUser.getPassword());
                session.update(existingUser);
            } else {
                System.out.println("Bill not found with id: " + updatedUser.getEmail());
            }
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        } finally {
        }
    }

}