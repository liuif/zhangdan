package cn.edu.zjut.po;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 13259
 */
public class ExpenseType implements java.io.Serializable{
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    private int typeId;
    private String typeName;
    private Set<Bill> bills = new HashSet<>();

    public ExpenseType() {}
    public ExpenseType(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
}