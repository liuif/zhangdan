package cn.edu.zjut.control;

import cn.edu.zjut.po.User;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.service.TypeService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 13259
 */
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class TypeController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    /**
     * 获取消费类型列表
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getExpenseType", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> getExpenseType(@RequestBody Map<String,Object> request) {
        List<String> expenseTypes = typeService.getAllExpenseType((String) request.get("email"));
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查找成功");
        result.put("data",expenseTypes);
        for (String expenseType : expenseTypes) {
            System.out.println(expenseType);
        }
        return result;
    }

    /**
     * 添加消费类型
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/addExpenseType", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> addExpenseType(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        UserExpenseType expenseType=new UserExpenseType();
        expenseType.setType_name((String)request.get("typename"));
        User user=userService.getUser((String) request.get("email"));
        expenseType.setUser(user);
        result.put("code",typeService.addExpenseType(expenseType));
        return result;
    }

    /**
     * 删除该消费类型
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/removeExpenseType", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> removeExpenseType(@RequestBody Map<String,Object> request) {
            Map<String, Object> result = new HashMap<>();
        UserExpenseType id=typeService.findtypeidbyname((String) request.get("typename"), (String) request.get("email"));
        result.put("code",typeService.removeExpenseType(id.getType_id()));
        return result;
    }
}


