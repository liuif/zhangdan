package cn.edu.zjut.service;

import cn.edu.zjut.po.Property;

import java.util.List;

public interface IPropertyService {
    public boolean addOneProperty(Property property);
    public boolean removeProperty(int id);
    public boolean updateOneProperty(Property property);
    public List getAllProperty(String email);
    public int findpropertyidbyname(String email,String roperty_name);
    public double getTotalAsset(String email);
}
