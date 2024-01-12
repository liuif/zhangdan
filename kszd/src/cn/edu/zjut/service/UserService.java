package cn.edu.zjut.service;

import cn.edu.zjut.dao.UserDAO;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service("userService")
public class UserService implements IUserService {
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 用户注册
     * @param loginUser
     * @return boolean
     */
    public boolean login(User loginUser) {
        String user_email = loginUser.getEmail();
        String user_password = loginUser.getPassword();
        String hql = "FROM User WHERE email = '" + user_email + "'";
        Session session = this.getSession();
        UserDAO dao = new UserDAO();
        dao.setSession(session);
        String password = dao.getUser(hql).getPassword();
        System.out.println(password);
        HibernateUtil.closeSession();
        return Objects.equals(password, user_password);
    }

    /**
     * 用户注册
     * @param loginUser
     * @return boolean
     */
    public boolean register(User loginUser) {
        Session session = this.getSession();
        UserDAO dao = new UserDAO();
        dao.setSession(session);
        Ledger ledger = new Ledger();
        ledger.setUser(loginUser);
        ledger.setLedgerCreateDate(LocalDateTime.now());
        ledger.setLedgerName("我的账本");
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.save(loginUser);
            dao.saveledger(ledger);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("save user failed " + e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 修改密码
     * @param user
     * @return boolean
     */
    public boolean updatePassword(User user) {
        Session session = this.getSession();
        UserDAO dao = new UserDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            System.out.println(tran);
            dao.update(user);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update user failed " + e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 根据email获取用户
     * @param email
     * @return User
     */
    public User getUser(String email) {
        String hql = "FROM User WHERE email = '" + email + "'";
        Session session = this.getSession();
        UserDAO dao = new UserDAO();
        dao.setSession(session);
        User user = dao.getUser(hql);
        return user;
    }
}