package cn.edu.zjut.dao;
import java.util.List;

import cn.edu.zjut.po.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.NoResultException;

public class BillDAO {
    private Log log = LogFactory.getLog(BillDAO.class);
    private Session session;
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 添加一条记录，并修改对应的总预算和预算明细
     * @param bill
     */
    public void save(Bill bill) {
        log.debug("saving bill instance");
        try {
            session.save(bill);
            log.debug("save successful");
            // 每次新增一条账本中的账单时，修改预算下的当前金额
            // 获取账本
            Ledger ledger = bill.getLedger();
            // 获取预算
            Budget budget = (Budget) session.createQuery(
                            "FROM Budget WHERE ledger = :ledger AND userexpensetype = :user_expenseType AND YEAR(period)=:year AND MONTH(period)=:month")
                    .setParameter("ledger", ledger)
                    .setParameter("user_expenseType", bill.getUserexpensetype())
                    .setParameter("year", bill.getBillCreateDate().getYear())
                    .setParameter("month", bill.getBillCreateDate().getMonth().getValue())
                    .uniqueResult();
            if (budget != null) {
                // 更新当前预算金额
                budget.setCurrentBudget(budget.getCurrentBudget() - bill.getMoney());
                session.update(budget);
            } else {
                log.warn("Budget not found for ledger: " + ledger.getLedgerId() +
                        ", userExpenseType: " + bill.getUserexpensetype().getType_id());
            }
            log.debug("update current budget and total budget successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally{
        }
    }

    /**
     * 删除账单，并修改对应的总预算和预算明细
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
                // 每次删除一条账本中的账单时，修改预算下的当前金额
                // 获取账本
                Ledger ledger = bill.getLedger();
                // 获取预算
                Budget budget = (Budget) session.createQuery(
                                "FROM Budget WHERE ledger = :ledger AND userexpensetype = :user_expenseType AND YEAR(period)=:year AND MONTH(period)=:month")
                        .setParameter("ledger", ledger)
                        .setParameter("user_expenseType", bill.getUserexpensetype())
                        .setParameter("year", bill.getBillCreateDate().getYear())
                        .setParameter("month", bill.getBillCreateDate().getMonth().getValue())
                        .uniqueResult();
                if (budget != null) {
                    // 更新当前预算金额
                    budget.setCurrentBudget(budget.getCurrentBudget() + bill.getMoney());
                    session.update(budget);
                } else {
                    log.warn("Budget not found for ledger: " + ledger.getLedgerId() +
                            ", userExpenseType: " + bill.getUserexpensetype().getType_id());
                }
                log.debug("update current budget and total budget successful");
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
     * 修改账单，并修改对应的总预算和预算明细
     * @param updatedBill
     */
    public void update(Bill updatedBill) {
        try {
            // 根据账单ID获取原始账单对象
            Bill existingBill = session.get(Bill.class, updatedBill.getBillId());
            double modifiedMoney = updatedBill.getMoney() - existingBill.getMoney();
            // 更新账单
            if (existingBill != null) {
                existingBill.setLedger(updatedBill.getLedger());
                existingBill.setMoney(updatedBill.getMoney());
                existingBill.setUserexpensetype(updatedBill.getUserexpensetype());
                existingBill.setIncomeOrExpense(updatedBill.getIncomeOrExpense());
                existingBill.setNote(updatedBill.getNote());
                existingBill.setBillCreateDate(updatedBill.getBillCreateDate());
                session.update(existingBill);
                // 每次修改一条账本中的账单时，修改预算下的当前金额
                // 获取账本
                Ledger ledger = existingBill.getLedger();
                // 获取预算
                Budget budget = (Budget) session.createQuery(
                                "FROM Budget WHERE ledger = :ledger AND userexpensetype = :user_expenseType AND YEAR(period)=:year AND MONTH(period)=:month")
                        .setParameter("ledger", ledger)
                        .setParameter("user_expenseType", existingBill.getUserexpensetype())
                        .setParameter("year", existingBill.getBillCreateDate().getYear())
                        .setParameter("month", existingBill.getBillCreateDate().getMonth().getValue())
                        .uniqueResult();
                if (budget != null) {
                    // 更新当前预算金额
                    budget.setCurrentBudget(budget.getCurrentBudget() - modifiedMoney);
                    session.update(budget);
                } else {
                    log.warn("Budget not found for ledger: " + ledger.getLedgerId() +
                            ", userExpenseType: " + existingBill.getUserexpensetype().getType_id());
                }
                log.debug("update current budget and total budget successful");
            } else {
                System.out.println("Bill not found with id: " + updatedBill.getBillId());
            }
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        } finally {
        }
    }

    /**
     * 根据hql语句查找bill
     * @param hql
     */
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

    /**
     * 根据hql语句查找bill，但未查到结果时返回0，专用于统计
     * @param hql
     */
    public Double statistics(String hql) {
        log.debug("finding bill instance by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            Object result = queryObject.uniqueResult();
            Double sum = result == null ? 0.0d : (double) result;
            System.out.println("bill is found");
            return sum;
        } catch (NoResultException ex) {
            System.out.println("No bill found");
            return 0.0d;
        } catch (Exception e) {
            log.error("find by hql failed", e);
            System.out.println("Error occurred while finding bill: " + e.getMessage());
            return 0.0d;
        }
    }


    /**
     * 根据hql语句查找bill
     * @param hql
     */
    public Bill getBill(String hql) {
        log.debug("get bill by hql");
        try {
            String queryString = hql;
            Query queryObject = session.createQuery(queryString);
            System.out.println("user is found");
            return (Bill) queryObject.getSingleResult();
        } catch (RuntimeException re) {
            log.error("find by hql failed", re);
            System.out.println("user is not found " + re);
            throw re;
        } finally {
        }
    }
}