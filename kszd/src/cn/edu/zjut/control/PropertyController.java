package cn.edu.zjut.control;

import cn.edu.zjut.po.Property;
import cn.edu.zjut.po.User;
import cn.edu.zjut.service.PropertyService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    /**
     * 获取所有资金账户
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getProperty", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> getProperty(@RequestBody Map<String,Object> request) {
        List<Property> propertys = propertyService.getAllProperty((String) request.get("email"));
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查找成功");
        List list=new ArrayList<>();
        for (Property property : propertys) {
            Map<String, Object> pro = new HashMap<>();
            pro.put("property_name",property.getProperty_name());
            pro.put("property_id",property.getProperty_id());
            pro.put("property_balance",property.getProperty_balance());
            list.add(pro);
        }
        result.put("data",list);
        return result;
    }

    /**
     * 添加资金账户
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/addProperty", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> addProperty(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Property property=new Property();
        User user =userService.getUser((String) request.get("email"));
        property.setProperty_balance(Double.parseDouble((String)(String) request.get("balance")));
        property.setProperty_name((String) request.get("property_name"));
        property.setUser(user);
        result.put("code",propertyService.addOneProperty(property));
        return result;
    }

    /**
     * 删除资金账户
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/removeProperty", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> removeProperty(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        int id=propertyService.findpropertyidbyname((String) request.get("email"),(String) request.get("type"));
        result.put("code",propertyService.removeProperty(id));
        return result;
    }

    /**
     * 获取总资产
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalBalance", method = RequestMethod.POST, consumes = "application/json")
    public  Map<String, Object> getTotalBalance(@RequestBody Map<String,Object> request) {
        double totalBalance = propertyService.getTotalAsset((String) request.get("email"));
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查找成功");
        result.put("data",totalBalance);
        return result;
    }

}