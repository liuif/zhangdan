package cn.edu.zjut.service;

import cn.edu.zjut.po.User;

/**
 * @author 13259
 */
public interface IUserService {
    public boolean login(User loginUser);
    public boolean register(User loginUser);
    public boolean updatePassword (User user);

}
