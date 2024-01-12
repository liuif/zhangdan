package cn.edu.zjut.control;

import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.service.TypeService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author 13259
 */
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    /**
     * 登录
     * @param user
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> login(@RequestBody User user) {
        //User user=new User(((String)request.get("email")),null,((String)request.get("password")))
        Map<String, Object> result = new HashMap<>();
        if (userService.login(user)) {
            Map<String, Object> USer = new HashMap<>();
            USer.put("email",user.getEmail());
            USer.put("username",userService.getUser(user.getEmail()).getName());
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

    /**
     * 注册
     * @param user
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=new Ledger();
        UserExpenseType expenseType=new UserExpenseType();
        ledger.setUser(user);
        ledger.setLedgerName("我的账本");
        user.addLedgers(ledger);
        if (userService.register(user)) {
            Map<String, Object> USer = new HashMap<>();
            USer.put("email",user.getEmail());
            USer.put("username",user.getName());
            USer.put("password",user.getPassword());
            expenseType.setType_name("餐饮美食");
            expenseType.setUser(user);
            typeService.addExpenseType(expenseType);
            expenseType.setType_name("文化教育");
            typeService.addExpenseType(expenseType);
            expenseType.setType_name("公共服务");
            typeService.addExpenseType(expenseType);
            result.put("code", 0);
            result.put("msg", "注册成功");
            result.put("data", USer);
        } else {
            result.put("code", -1);
            result.put("msg", "注册失败");
        }
        return result;
    }

    /**
     * 修改密码
     * @param user
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/update_password", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> update_password(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        if (userService.updatePassword(user)) {
            Map<String, Object> USer = new HashMap<>();
            USer.put("email",user.getEmail());
            USer.put("username",userService.getUser(user.getEmail()).getName());
            USer.put("password",user.getPassword());
            result.put("code", 0);
            result.put("msg", "修改成功");
            result.put("data", USer);
        } else {
            result.put("code", -1);
            result.put("msg", "修改失败");
        }
        return result;
    }

    /**
     * 找回密码
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/findpassword", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> findpassword(@RequestBody Map<String,Object> request) throws MessagingException {
        Map<String, Object> result = new HashMap<>();
        //	账号信息
        try {
            String username = "liuif12138@163.com";
            String password = "WCUCTIJJSHKZNDYI";
            System.out.println(1);
            //	创建一个配置文件，并保存
            Properties props = new Properties();

            //	SMTP服务器连接信息
            //  126——smtp.126.com
            //  163——smtp.163.com
            //  qqsmtp.qq.com"
            props.put("mail.smtp.host", "smtp.163.com");
            System.out.println(2);
            //  126——25
            //  163——465
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            System.out.println(3);
            //	创建session会话
            //	参数1:smtp服务器连接参数
            //	参数2:账号和密码的授权认证对象
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            System.out.println(session);
            System.out.println(4);
            MimeMessage message = new MimeMessage(session);
            message.setSubject("找回密码——验证码");
            message.setText("验证码:"+request.get("code"));
            message.setFrom(new InternetAddress("liuif12138@163.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress((String) request.get("email")));
            System.out.println(5);
//	发送
            Transport.send(message);
            System.out.println(6);
        }catch (MessagingException e){
            result.put("code",1);
            return result;
        }
        result.put("code",0);
        return result;
    }
}