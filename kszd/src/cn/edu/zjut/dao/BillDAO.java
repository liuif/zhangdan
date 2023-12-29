package cn.edu.zjut.dao;
import java.util.List;

import cn.edu.zjut.po.ExpenseType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cn.edu.zjut.po.Bill;

public class BillDAO {
    private Log log = LogFactory.getLog(BillDAO.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 添加一条记录
     * @param bill
     */
    public void save(Bill bill) {
        log.debug("saving bill instance");
        try {
            session.save(bill);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally{
        }
    }
    /**
     * 删除账单
     * @param billId
     */
    public void delete(int billId) {
        log.debug("delete bill instance");
        try {
            // 根据账单ID获取账单对象
            Bill bill = session.get(Bill.class, billId);
            // 删除账单
            if (bill != null) {
                session.delete(bill);
            } else {
                System.out.println("Bill not found with id: " + billId);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }


    /**
     * 修改账单
     * @param updatedBill
     */
    public void update(Bill updatedBill) {
        try {
            // 根据账单ID获取原始账单对象
            Bill existingBill = session.get(Bill.class, updatedBill.getBillId());

            // 更新账单
            if (existingBill != null) {
                existingBill.setLedger(updatedBill.getLedger());
                existingBill.setMoney(updatedBill.getMoney());
                existingBill.setExpenseType(updatedBill.getExpenseType());
                existingBill.setIncomeOrExpense(updatedBill.getIncomeOrExpense());
                existingBill.setNote(updatedBill.getNote());
                existingBill.setBillCreateDate(updatedBill.getBillCreateDate());
                session.update(existingBill);
            } else {
                System.out.println("Bill not found with id: " + updatedBill.getBillId());
            }
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        } finally {
        }
    }
    public List findByHql(String hql) {
        log.debug("finding bill instance by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            List result = queryObject.getResultList();
            System.out.println("bill is found");
            return result;
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("bill is not found "+re);
            throw re;
        } finally{
        }
    }

    public List findAllType() {
        log.debug("finding all ExpenseType instances");
        try {
            String queryString = "select typeName from ExpenseType ";
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
     * @param
     */
    public void addExpenseType(ExpenseType type) {
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
     * @param
     */
    public void deleteExpenseType(int typeId) {
        log.debug("delete bill instance");
        try {
            // 根据账单ID获取账单对象
            ExpenseType type = session.get(ExpenseType.class, typeId);
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

    public int findidbyname(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (int) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }
}