package cn.edu.zjut.service;

import cn.edu.zjut.dao.PropertyDAO;
import cn.edu.zjut.po.Property;
import cn.edu.zjut.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("propertyService")
public class PropertyService implements  IPropertyService{

    /**
     * 获得session
     * @return
     */
    public Session getSession() {
        return HibernateUtil.getSession();
    }

    /**
     * 添加一条资金账户
     * @param property
     * @return boolean
     */
    public boolean addOneProperty(Property property) {
        Session session=this.getSession();
        PropertyDAO dao = new PropertyDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.save(property);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("save property failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 删除一条资金账户
     * @param id
     * @return boolean
     */
    public boolean removeProperty(int id) {
        Session session=this.getSession();
        PropertyDAO dao=new PropertyDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.delete(id);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("delete property failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 添加一条资金账户
     * @param property
     * @return boolean
     */
    public boolean updateOneProperty(Property property) {
        Session session=this.getSession();
        PropertyDAO dao = new PropertyDAO();
        dao.setSession(session);
        Transaction tran = null;
        try {
            tran = session.beginTransaction();
            dao.update(property);
            tran.commit();
            return true;
        } catch (Exception e) {
            System.out.println("update property failed "+ e);
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * 获取所有资金账户
     * @param email
     * @return List
     */
    public List getAllProperty(String email) {
        Session session=this.getSession();
        PropertyDAO propertyDAO= new PropertyDAO();
        propertyDAO.setSession(session);
        List propertys = propertyDAO.findAllProperty(email);
        HibernateUtil.closeSession();
        return propertys;
    }

    /**
     * 根据资金账户名查找资金账户
     * @param email,property_name
     * @return int
     */
    public int findpropertyidbyname(String email,String property_name) {
        String hql = "SELECT p.property_id FROM Property p WHERE p.property_name = '"
                +property_name +"' and p.user.email = '"+email+"'";
        Session session=this.getSession();
        PropertyDAO dao=new PropertyDAO();
        dao.setSession(session);
        int property_id = dao.findidbyname(hql);
        System.out.println(property_id);
        HibernateUtil.closeSession();
        return property_id;
    }

    /**
     * 获取资金账户之和
     * @param email
     * @return double
     */
    public double getTotalAsset(String email) {
        String hql = "SELECT SUM(p.property_balance) FROM Property p where p.user.email='"+email+"'";
        Session session=this.getSession();
        double totalAsset = 0;
        PropertyDAO dao=new PropertyDAO();
        dao.setSession(session);
        totalAsset=dao.findTotalBalance(hql);
        System.out.println(totalAsset);
        HibernateUtil.closeSession();
        return totalAsset;
    }

}