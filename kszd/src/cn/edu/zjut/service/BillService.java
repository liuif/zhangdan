package cn.edu.zjut.service;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.edu.zjut.dao.BillDAO;
import cn.edu.zjut.dao.UserDAO;
import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.User;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import cn.edu.zjut.po.Bill;

import cn.edu.zjut.util.HibernateUtil;
import org.springframework.stereotype.Service;

/**
 * @author b7
 */
@Service("billService")
public class BillService implements IBillService{
    /**
     * 获得session
     * @return
     */
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 添加一条账单记录
     * @param bill
     * @return
     */
    public boolean addOneBill(Bill bill) {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.save(bill);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("save bill failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }
    /**
     * 删除一条账单记录
     * @param bill
     * @return
     */
    public boolean removeOneBill(Bill bill) {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.delete(bill.getBillId());
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete bill failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 修改一条账单记录
     * @param bill
     * @return
     */
    public boolean updateOneBill(Bill bill) {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.update(bill);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update bill failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 删除一组账单
     * @param bills
     * @return
     */
    public boolean removeBills(List<Bill> bills) {
        Session session = this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            for (Bill bill : bills) {
                dao.delete(bill.getBillId());
            }
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete bills failed " + e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<Object[]> selectByDate(LocalDate date,String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql="select bill.money,bill.expenseType.typeName,bill.incomeOrExpense,bill.note "
                + " from Bill bill "
                + " where YEAR(bill.billCreateDate) = "+"'"+date.getYear()+"'"
                + " and MONTH(bill.billCreateDate) = "+"'"+date.getMonth().getValue()+"'"
                + " and DAY(bill.billCreateDate) = "+"'"+date.getDayOfMonth()+"'"
                + " and bill.ledger.user.email ="+"'"+email+"'";
        List list = dao.findByHql(hql);
        HibernateUtil.closeSession();
        return list;
    }

    public List<Object[]> selectByType(String type,String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select money,bill.expenseType.typeName,incomeOrExpense,note "
                + "from Bill bill "
                + "where bill.expenseType.typeName=" + "'"+type+ "'"
                +" and bill.ledger.user.email ="+"'"+email+"'";
        List list = dao.findByHql(hql);
        HibernateUtil.closeSession();
        return list;
    }

    public List getAllExpenseType() {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        List items = dao.findAllType();
        HibernateUtil.closeSession();
        return items;
    }
    /**
     * 添加一条账单记录
     * @param
     * @return
     */
    public boolean addExpenseType(ExpenseType expenseType) {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.addExpenseType(expenseType);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("save ExpenseType failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }
    /**
     * 删除一条账单记录
     * @param
     * @return
     */
    public boolean removeExpenseType(int type) {
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.deleteExpenseType(type);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete ExpenseType failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public int findtypeidbyname(String typeName) {
        String hql = "SELECT typeId FROM ExpenseType WHERE typeName = '"
                +typeName +"'";
        Session session=this.getSession();
        BillDAO dao=new BillDAO();
        dao.setSession(session);
        int typeId = dao.findidbyname(hql);
        System.out.println(typeId);
        HibernateUtil.closeSession();
        return typeId;
    }
}