package cn.edu.zjut.dao;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Ledger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class BudgetDAO {
    private Log log = LogFactory.getLog(BudgetDAO.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 设置总预算
     * @param updatedLedger,budget,currentbudget
     */
    public void setTotalBudget(Ledger updatedLedger,double budget,double currentbudget) {
        try{
            // 根据账本ID获取原始账单对象
            Ledger existingLedger = session.get(Ledger.class, updatedLedger.getLedgerId());

            // 更新账本预算
            if (existingLedger != null) {
                existingLedger.setBudget(budget);
                existingLedger.setCurrent_budget(currentbudget);
                session.update(existingLedger);
            } else {
                System.out.println("Ledger not found with id: " + updatedLedger.getLedgerId());
            }
        } catch (RuntimeException re) {
            log.error("update budget failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 设置预算明细，相当于新增一条记录
     * @param budget
     */
    public void setBudgetDetails(Budget budget) {
        log.debug("saving Budget instance");
        try {
            session.save(budget);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 根据hql查找budget
     * @param hql
     */
    public List getBudget(String hql) {
        log.debug("get user_password by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (List) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }

    /**
     * 根据hql查找所有budget
     * @param hql
     */
    public List getTotalBudget(String hql) {
        log.debug("finding Budget instance by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("Budget is found");
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("budget is not found "+re);
            throw re;
        } finally{
        }
    }

    /**
     * 根据账本id、消费类型id和日期删除预算
     * @param ledger_id,type_id,period
     */
    public void delete(int ledger_id, int type_id, LocalDate period) {
        log.debug("delete ledger instance");
        try {
            System.out.println(ledger_id);
            System.out.println(type_id);
            System.out.println(period);
            String hql = "FROM Budget b WHERE b.ledger.ledgerId = :ledgerId AND b.userexpensetype.type_id = :typeId AND YEAR(b.period) = :year AND MONTH(b.period) = :month ";
            Query<Budget> query = session.createQuery(hql, Budget.class);
            query.setParameter("ledgerId", ledger_id);
            query.setParameter("typeId", type_id);
            query.setParameter("year", period.getYear());
            query.setParameter("month", period.getMonth().getValue());

            System.out.println(hql);

            Budget budget = query.uniqueResult();
            System.out.println(budget.getBudget());

            // 删除账本
            if (budget != null) {
                session.delete(budget);
            } else {
                System.out.println("Budget not found with id: " + ledger_id + ", " + type_id + ", " + period);
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
        }
    }
}