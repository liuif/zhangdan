package cn.edu.zjut.control;

import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 13259
 */
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> login(@RequestBody User user) {
        //User user=new User(((String)request.get("email")),null,((String)request.get("password")))
        Map<String, Object> result = new HashMap<>();
        if (userService.login(user)) {
            Map<String, Object> USer = new HashMap<>();
            USer.put("email",user.getEmail());
            USer.put("username",userService.getname(user.getEmail()));
            USer.put("password",user.getPassword());
            result.put("code", 0);
            result.put("msg", "登录成功");
            result.put("data", USer);
        } else {
            result.put("code", -1);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=new Ledger();
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        ledger.setUser(user);
        ledger.setLedgerName("我的账本");
        user.addLedgers(ledger);
        if (userService.register(user)) {
            Map<String, Object> USer = new HashMap<>();
            USer.put("email",user.getEmail());
            USer.put("username",userService.getname(user.getEmail()));
            USer.put("password",user.getPassword());
            result.put("code", 0);
            result.put("msg", "注册成功");
            result.put("data", USer);
        } else {
            result.put("code", -1);
            result.put("msg", "注册失败");
        }
        return result;
    }
}