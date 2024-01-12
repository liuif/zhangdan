package cn.edu.zjut.service;

import cn.edu.zjut.dao.BillDAO;
import cn.edu.zjut.dao.TypeDAO;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 13259
 */
@Service("typeService")
public class TypeService implements ITypeService{
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 获取所有消费类型的名字
     * @param userId
     * @return List
     */
    public List getAllExpenseType(String userId) {
        Session session=this.getSession();
        TypeDAO dao = new TypeDAO();
        dao.setSession(session);
        List items = dao.findAllType(userId);
        HibernateUtil.closeSession();
        return items;
    }

    /**
     * 获取所有消费类型
     * @param userId
     * @return List<UserExpenseType>
     */
    public List<UserExpenseType> getAllExpenseTypeID(String userId) {
        Session session=this.getSession();
        TypeDAO dao = new TypeDAO();
        dao.setSession(session);
        List items = dao.findAllTypeID(userId);
        HibernateUtil.closeSession();
        return items;
    }

    /**
     * 添加一条type
     * @param expenseType
     * @return boolean
     */
    public boolean addExpenseType(UserExpenseType expenseType) {
        Session session=this.getSession();
        TypeDAO dao = new TypeDAO();
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
     * 删除一条type
     * @param int
     * @return boolean
     */
    public boolean removeExpenseType(int type) {
        Session session=this.getSession();
        TypeDAO dao = new TypeDAO();
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

    /**
     * 根据消费类型名称寻找消费类型
     * @param typeName，email
     * @return UserExpenseType
     */
    public UserExpenseType findtypeidbyname(String typeName,String email) {
        String hql = "FROM UserExpenseType type WHERE type.type_name = '"
                + typeName + "' and type.user.email='" + email + "'";
        Session session=this.getSession();
        TypeDAO dao=new TypeDAO();
        dao.setSession(session);
        UserExpenseType typeId = dao.findidbyname(hql);
        System.out.println(typeId);
        HibernateUtil.closeSession();
        return typeId;
    }
}
