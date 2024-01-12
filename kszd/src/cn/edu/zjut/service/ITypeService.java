package cn.edu.zjut.service;

import cn.edu.zjut.po.UserExpenseType;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 13259
 */
public interface ITypeService {
    /**
     * $getAllExpenseType $获取所有消费类型
     * @author $13259
     * @version
     **/
    public List getAllExpenseType(String email);

    public boolean addExpenseType(UserExpenseType expenseType);

    public boolean removeExpenseType(int type);

    public UserExpenseType findtypeidbyname(String typeName, String email);
}
