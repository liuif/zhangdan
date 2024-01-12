package cn.edu.zjut.control;

import cn.edu.zjut.dao.LedgerDAO;
import cn.edu.zjut.dao.SharedDAO;
import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.Shared;
import cn.edu.zjut.po.User;
import cn.edu.zjut.service.BillService;
import cn.edu.zjut.service.LedgerService;
import cn.edu.zjut.service.SharedService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class SharedController {

    @Autowired
    private SharedService sharedService;

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private UserService userService;

    /**
     * 获取账本列表
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getledger", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getledger(@RequestBody Map<String,Object> request) {
        String email = (String) request.get("email");;
        List<Ledger> lists1=sharedService.getAllLedger(email);
        List<Ledger> lists2=ledgerService.getAllLedger(email);
        Map<String, Object> result = new HashMap<>();
        List ledgers1 = new ArrayList<>();;
        List ledgers2 = new ArrayList<>();;
        result.put("code", 0);
        result.put("msg", "查找成功");
        System.out.println("00000000");
        result.put("ledger_id", lists2.get(0).getLedgerId());
        System.out.println("111111111");
        for (Ledger row : lists2) {
            Map<String, Object> res = new HashMap<>();
            res.put("ledger_id",row.getLedgerId());
            res.put("ledger_name",row.getLedgerName());
            ledgers1.add(res);
            System.out.println(row.getLedgerName());
        }
        for (Ledger row : lists1) {
            Map<String, Object> res = new HashMap<>();
            res.put("ledger_id",row.getLedgerId());
            res.put("ledger_name","共享:"+row.getLedgerName());
            ledgers2.add(res);
            System.out.println(row.getLedgerName());
        }
        result.put("ledger",ledgers1);
        result.put("shared",ledgers2);
        return result;
    }

    /**
     * 添加新账本
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/addledger", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> addledger(@RequestBody Map<String,Object> request) {
        User user=userService.getUser((String) request.get("email"));
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=new Ledger();
        ledger.setUser(user);
        ledger.setLedgerName((String) request.get("ledgerName"));
        ledger.setLedgerCreateDate(LocalDateTime.now());
        if(ledgerService.addLedger(ledger)){
            result.put("code",0);
            return result;
        }else {
            result.put("code",1);
            return result;
        }
    }

    /**
     * 加入他人账本
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/joinLedger", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> joinLedger(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Shared shared=new Shared();
        User user =userService.getUser((String) request.get("email"));
        shared.setUser(user);
        shared.setAllowed(0);
        Ledger ledger=ledgerService.getLedgerbyinvitation((String) request.get("invitation"));
        shared.setLedger(ledger);
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
            message.setSubject("共享账本——申请");
            message.setText("用户："+user.getName()+" 邮箱："+user.getEmail()+"正在申请您的账本："+ledger.getLedgerName());
            message.setFrom(new InternetAddress("liuif12138@163.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(ledger.getUser().getEmail()));
            System.out.println(5);
//	发送
            Transport.send(message);
            System.out.println(6);
        }catch (MessagingException e){
            result.put("Em",1);
            return result;
        }

        result.put("Em",0);

        if(sharedService.joinLedger(shared)){
            result.put("code",0);
        }
        else {
            result.put("code",1);
        }
        return result;
    }

    /**
     * 删除账本
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/removeLedger", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> removeLedger(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        String ledgerName=(String) request.get("ledgerName");
        if("共享:".equals(ledgerName.substring(0, 3))){
            Shared shared=sharedService.getSharedbyid((Integer) request.get("ledgerId"),(String) request.get("email"));
            if(sharedService.removeOneLedger(shared.getId())){
                result.put("code",0);
            }else {result.put("code",1);}
        }else {
            if(ledgerService.removeOneLedger((Integer) request.get("ledgerId"))){
                result.put("code",0);
            }
            else {result.put("code",1);}
        }
        return result;
    }

    /**
     * 获取邀请码
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getinvitation", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getinvitation(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        String ledgerName=(String) request.get("ledgerName");
        Ledger ledger=ledgerService.getLedger((Integer) request.get("ledgerId"));
        result.put("invitation",ledger.getInvitation());
        result.put("code",0);
        return result;
    }

    /**
     * 获取账本名
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getledgertname", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getledgertname(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        int ledger_id=(int) request.get("ledger_id");
        Ledger ledger=ledgerService.getLedger(ledger_id);
        if(sharedService.checkIfSharedExists(ledger_id)){
            result.put("ledgername","共享:"+ledger.getLedgerName());
        }
        else {
            result.put("ledgername",ledger.getLedgerName());
        }
        return result;
    }

    /**
     * 修改allowed为1，表示允许用户加入账本
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/updateSharedLedger", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> updateSharedLedger(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Shared shared=sharedService.getShared((int) request.get("id"));
        // 假设您有一个服务实例，请根据实际情况调整
        if((int) request.get("code")==1){
            shared.setAllowed(1);
            if (sharedService.updateSharedLedger(shared)) {
                result.put("code", 0);
                result.put("message", "添加成功");
            } else {
                result.put("code", 1);
                result.put("message", "添加失败");
            }
        }else {
            if (sharedService.removeOneLedger(shared.getId())) {
                result.put("code", 0);
                result.put("message", "删除成功");
            } else {
                result.put("code", 1);
                result.put("message", "删除失败");
            }
        }
        return result;
    }

    /**
     * 判断当前账本是否是共享账本
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/checkIfShared", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> checkIfShared(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        if(sharedService.checkIfShared((int) request.get("ledger_id"), (String) request.get("email"))){
            result.put("code",1);
        }
        else {
            result.put("code",0);
        }
        return result;
    }

    /**
     * 获得申请人列表
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getapplicant", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getapplicant(@RequestBody Map<String,Object> request) {
        String email = (String) request.get("email");;
        List<Object[]> lists=sharedService.getApplicant(email);
        Map<String, Object> result = new HashMap<>();
        List ledgers1 = new ArrayList<>();;
        result.put("code", 0);
        result.put("msg", "查找成功");
        List share=new ArrayList<>();
        for (Object[] row : lists) {
            Map<String, Object> shared = new HashMap<>();
            shared.put("email",row[0]);
            shared.put("name",row[1]);
            shared.put("id",row[2]);
            shared.put("ledgername",row[3]);
            share.add(shared);
        }
        result.put("users",share);
        return result;
    }

}


