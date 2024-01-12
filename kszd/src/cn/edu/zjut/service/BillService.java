package cn.edu.zjut.service;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import cn.edu.zjut.dao.BillDAO;
import cn.edu.zjut.po.Bill;
import cn.edu.zjut.util.HibernateUtil;

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
     * 根据id获取对应的账单
     * @param billid
     * @return Bill
     */
    public Bill getBill(int billid){
        String hql = "FROM Bill WHERE billId = '"
                +billid +"'";
        Session session=this.getSession();
        BillDAO dao=new BillDAO();
        dao.setSession(session);
        Bill bill=dao.getBill(hql);
        return bill;
    }

    /**
     * 添加一条账单记录
     * @param bill
     * @return boolean
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
     * @return boolean
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
     * @return boolean
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
     * @param List<Bill> bills
     * @return boolean
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

    /**
     * 根据日期查找账单
     * @param date,ledger_id
     * @return List<Object[]>
     */
    public List<Object[]> selectByDate(LocalDate date,int ledger_id){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql="select bill.money,bill.userexpensetype.type_name,bill.incomeOrExpense,bill.note,bill.billId,bill.user.name "
                + " from Bill bill "
                + " where YEAR(bill.billCreateDate) = "+"'"+date.getYear()+"'"
                + " and MONTH(bill.billCreateDate) = "+"'"+date.getMonth().getValue()+"'"
                + " and DAY(bill.billCreateDate) = "+"'"+date.getDayOfMonth()+"'"
                + " and bill.ledger.ledgerId ="+"'"+ledger_id+"'";
        List list = dao.findByHql(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月的全部收入
     * @param year,email,month
     * @return Double
     */
    public Double INgetTotalIncomeAndExpenseByYear(int year, String email,int month){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '收入' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月的全部支出
     * @param year,email,month
     * @return Double
     */
    public Double OUTgetTotalIncomeAndExpenseByYear(int year, String email,int month){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '支出' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月该日的全部收入
     * @param year,email,month,day
     * @return Double
     */
    public Double INgetTotalIncomeAndExpenseByMonth(int year, String email,int month,int day){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '收入' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and DAY(bill.billCreateDate) = '"+day+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月该日的全部支出
     * @param year,email,month,day
     * @return Double
     */
    public Double OUTgetTotalIncomeAndExpenseByMonth(int year, String email,int month,int day){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '支出' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and DAY(bill.billCreateDate) = '"+day+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该消费类型的总收入
     * @param year,email,type_id
     * @return Double
     */
    public Double INfindDailyIncomeExpenseNetByYear(int year, int type_id, String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '收入' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and bill.userexpensetype.type_id = '"+type_id+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该消费类型的总支出
     * @param year,email,type_id
     * @return Double
     */
    public Double OUTfindDailyIncomeExpenseNetByYear(int year, int type_id, String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '支出' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and bill.userexpensetype.type_id = '"+type_id+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月该消费类型的总收入
     * @param year,email,month,type_id
     * @return Double
     */
    public Double INfindDailyIncomeExpenseNetByMonth(int year, int month, int type_id, String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '收入' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and bill.userexpensetype.type_id = '"+type_id+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该用户该年该月该消费类型的总支出
     * @param year,email,month,type_id
     * @return Double
     */
    public Double OUTfindDailyIncomeExpenseNetByMonth(int year, int month,int type_id, String email){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '支出' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and MONTH(bill.billCreateDate) = '"+month+"' "
                + "and bill.userexpensetype.type_id = '"+type_id+"' "
                + "and bill.ledger.user.email = '"+email+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }

    /**
     * 总结该年该账本的全部支出
     * @param year,ledger_id
     * @return Double
     */
    public Double CountAllbyYear(int year,int ledger_id){
        Session session=this.getSession();
        BillDAO dao = new BillDAO();
        dao.setSession(session);
        String hql = "select SUM(CASE WHEN bill.incomeOrExpense = '支出' THEN bill.money ELSE 0 END) "
                + "from Bill bill "
                + "where YEAR(bill.billCreateDate) = '"+year+"' "
                + "and bill.ledger.ledgerId = '"+ledger_id+"' ";
        Double list = dao.statistics(hql);
        HibernateUtil.closeSession();
        return list;
    }
}