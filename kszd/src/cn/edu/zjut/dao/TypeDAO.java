package cn.edu.zjut.dao;

import cn.edu.zjut.po.UserExpenseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * @author 13259
 */
public class TypeDAO {
    private Log log = LogFactory.getLog(TypeDAO.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 获取全部消费类型
     * @param user_id
     */
    public List findAllType(String user_id) {
        log.debug("finding all ExpenseType instances");
        try {
            String queryString = "select type_name from UserExpenseType where user.email="+"'"+user_id+"'";
            Query queryObject = session.createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 添加一条消费类型
     * @param type
     */
    public void addExpenseType(UserExpenseType type) {
        log.debug("saving bill instance");
        try {
            session.save(type);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 删除一条消费类型
     * @param typeId
     */
    public void deleteExpenseType(int typeId) {
        log.debug("delete bill instance");
        try {
            // 根据账单ID获取账单对象
            UserExpenseType type = session.get(UserExpenseType.class, typeId);
            // 删除账单
            if (type != null) {
                session.delete(type);
            } else {
                System.out.println("Bill not found with typeId: " + typeId);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 根据消费类型名查找消费类型
     * @param hql
     */
    public UserExpenseType findidbyname(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (UserExpenseType) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 查找该用户的所有消费类型的id
     * @param user_id
     */
    public List findAllTypeID(String user_id) {
        log.debug("finding all ExpenseType instances");
        try {
            String queryString = "from UserExpenseType where user.email="+"'"+user_id+"'";
            Query queryObject = session.createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally{
        }
    }
}
