package cn.edu.zjut.dao;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Shared;
import cn.edu.zjut.po.Ledger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class SharedDAO {

    private Log log = LogFactory.getLog(SharedDAO.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 在共享账本中添加一条记录，来记录谁被允许加入该共享账本
     * @param shared
     */
    public void save(Shared shared) {
        log.debug("saving shared instance");
        try {
            session.save(shared);
            log.debug("save shared successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 修改共享账本中的allowed属性
     * @param updateShared
     */
    public void updateAllowed(Shared updateShared) {
        try {
            // 根据共享账本ID获取原始共享账本对象
            Shared existingShared = session.get(Shared.class, updateShared.getId());
            // 更新allowed为1
            if (existingShared != null) {
                existingShared.setId(updateShared.getId());
                existingShared.setLedger(updateShared.getLedger());
                existingShared.setUser(updateShared.getUser());
                existingShared.setAllowed(updateShared.getAllowed());
                session.update(updateShared);
            } else {
                System.out.println("Shared ledger not found with id: " + updateShared.getId());
            }
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 删除该共享账本记录
     * @param Id
     */
    public void delete(int Id) {
        log.debug("delete ledger instance");
        try {
            // 根据账单ID获取账单对象
            Shared shared = session.get(Shared.class, Id);
            // 删除账本
            if (shared != null) {
                session.delete(shared);
            } else {
                System.out.println("Ledger not found with id: " + Id);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 获取该用户的全部共享账本
     * @param email
     */
    public List<Ledger> findAllLedger(String email) {
        log.debug("finding all Shared instances");
        try {
            String queryString = "select s.ledger from Shared s where s.user.email="+"'"+email+"' and s.allowed=1";
            Query queryObject = session.createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 获取该共享记录
     * @param hql
     */
    public Shared getShared(String hql) {
        log.debug("get shared by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (Shared) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 判断是否为共享账本
     * @param hql
     */
    public boolean checkIfSharedExists(String hql) {
        log.debug("get shared by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("share is found");
            Shared shared = (Shared) queryObject.uniqueResult();
            return shared != null;
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
            // 在这里可以进行一些资源的释放操作
        }
    }
}
