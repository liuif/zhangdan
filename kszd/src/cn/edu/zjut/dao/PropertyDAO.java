package cn.edu.zjut.dao;

import cn.edu.zjut.po.Property;
import cn.edu.zjut.po.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class PropertyDAO {

    private Log log = LogFactory.getLog(PropertyDAO.class);
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 添加一条资金账户
     * @param property
     */
    public void save(Property property) {
        log.debug("saving property instance");
        try {
            session.save(property);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 删除该资金账户
     * @param property_id
     */
    public void delete(int property_id) {
        log.debug("delete bill instance");
        try {
            // 根据账单ID获取账单对象
            Property property = session.get(Property.class, property_id);
            // 删除账单
            if (property != null) {
                session.delete(property);
            } else {
                System.out.println("Bill not found with id: " + property_id);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }


    /**
     * 更新该资金账户
     * @param updatedProperty
     */
    public void update(Property updatedProperty) {
        try {
            // 根据账单ID获取原始账单对象
            Property existingProperty = session.get(Property.class, updatedProperty.getProperty_id());

            // 更新账单
            if (existingProperty != null) {
                existingProperty.setProperty_name(updatedProperty.getProperty_name());
                existingProperty.setProperty_balance(updatedProperty.getProperty_balance());
                session.update(existingProperty);
            } else {
                System.out.println("Property not foundwith id: " + updatedProperty.getProperty_id());
            }
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 查找该用户所有资金账户
     * @param email
     */
    public List findAllProperty(String email) {
        log.debug("finding all property instances");
        try {
            String queryString = "from Property p where p.user.email= '"+email+"'";
            Query queryObject = session.createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 根据资金账户名查找id
     * @param hql
     */
    public int findidbyname(String hql) {
        log.debug("get  by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println(" found");
            return (int) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 获取全部资金账户的和
     * @param hql
     */
    public double findTotalBalance(String hql) {
        log.debug("get TotalBalance by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            Double totalBalance = (Double) queryObject.uniqueResult();
            return (double) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("not found " + re);
            throw re;
        } finally {
        }
    }
}