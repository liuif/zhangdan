package cn.edu.zjut.service;

import cn.edu.zjut.dao.BillDAO;
import cn.edu.zjut.dao.BudgetDAO;
import cn.edu.zjut.dao.LedgerDAO;
import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service("budgetService")
public class BudgetService implements IBudgetService{
    /**
     * 获得session
     * @return
     */
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 更新总预算
     * @param ledger,list
     * @return boolean
     */
    public boolean updateTotalBudget(Ledger ledger,List list) {
        Session session=this.getSession();
        BudgetDAO dao = new BudgetDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.setTotalBudget(ledger, (Double) list.get(0),(Double) list.get(1));
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update ledger total budget failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 获取总预算
     * @param ledgerid,date
     * @return List<Object[]>
     */
    public List<Object[]> getTotalBudget(int ledgerid, LocalDate date){
        Session session=this.getSession();
        BudgetDAO dao = new BudgetDAO();
        dao.setSession(session);
        String hql = "select b.userexpensetype.type_name,b.userexpensetype.type_id,b.budget,b.currentbudget " +
                "from Budget b where b.ledger.ledgerId = '"+ledgerid+"'"+
                " and YEAR(b.period) = "+"'"+date.getYear()+"'"+
                " and MONTH(b.period) = "+"'"+date.getMonth().getValue()+"'";
        List<Object[]> list = dao.getTotalBudget(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 删除预算
     * @param ledger_id,type_id,period
     * @return boolean
     */
    public boolean removeBudget(int ledger_id, int type_id, LocalDate period) {
        Session session=this.getSession();
        BudgetDAO dao = new BudgetDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.delete(ledger_id,type_id,period);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete ledger failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 设添加新预算
     * @param budget
     * @return boolean
     */
    public boolean updateBudgetDetails(Budget budget) {
        Session session=this.getSession();
        BudgetDAO dao = new BudgetDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.setBudgetDetails(budget);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update budget details failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 根据账本和日期获取预算
     * @param ledger,date
     * @return List
     */
    public List getBudget(Ledger ledger, LocalDate date){
        String hql = "SELECT SUM(b.budget)，SUM(b.currentbudget) FROM Budget WHERE ledger.ledgerId = '"
                +ledger.getLedgerId() +"'"+
                " and YEAR(b.period) = 2024"+
                " and MONTH(b.period) = 01";
        Session session=this.getSession();
        BudgetDAO dao=new BudgetDAO();
        dao.setSession(session);
        List budget=dao.getBudget(hql);
        return budget;
    }
}