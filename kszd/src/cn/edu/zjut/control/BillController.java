package cn.edu.zjut.control;

import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.service.BillService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 13259
 */
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class BillController {
        @Autowired
        private BillService billService;

        @ResponseBody
        @RequestMapping(value = "/getbillsbydata", method = RequestMethod.POST, consumes = "application/json")
        public Map<String, Object> getbillsbydata(@RequestBody Map<String,Object> request) {
            LocalDate data= LocalDate.parse((CharSequence) request.get("data"));
            String email = (String) request.get("email");
            System.out.println(data);
            System.out.println(email);
            List<Object[]> lists=billService.selectByDate(data,email);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> bills = new ArrayList<>();
            result.put("code", 0);
            result.put("msg", "查找成功");
            for (Object[] row : lists) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("money",row[0]);
                bill.put("typeName",row[1]);
                bill.put("incomeOrExpense",row[2]);
                bill.put("note",row[3]);
                bills.add(bill);
                System.out.println(bills);
            }
            result.put("data",bills);
            return result;
        }

        @ResponseBody
        @RequestMapping(value = "/addbill", method = RequestMethod.POST, consumes = "application/json")
        public Map<String, Object> addbill(@RequestBody User user) {
            //User user=new User(((String)request.get("email")),null,((String)request.get("password")))
            Map<String, Object> result = new HashMap<>();
            Ledger ledger=new Ledger();
            System.out.println(user.getName());
            System.out.println(user.getEmail());
            System.out.println(user.getPassword());
            ledger.setUser(user);
            ledger.setLedgerName("我的账本");
            user.addLedgers(ledger);

            return result;
        }

        @ResponseBody
        @RequestMapping(value = "/getExpenseType", method = RequestMethod.POST, consumes = "application/json")
        public  Map<String, Object> getExpenseType() {
            List<String> expenseTypes = billService.getAllExpenseType();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "查找成功");
            result.put("data",expenseTypes);
            for (String expenseType : expenseTypes) {
                System.out.println(expenseType);
            }
            return result;
        }

        @ResponseBody
        @RequestMapping(value = "/addExpenseType", method = RequestMethod.POST, consumes = "application/json")
        public  Map<String, Object> addExpenseType(@RequestBody Map<String,Object> request) {
            Map<String, Object> result = new HashMap<>();
            ExpenseType expenseType=new ExpenseType();
            expenseType.setTypeName((String)request.get("typename"));
            result.put("code",billService.addExpenseType(expenseType));
            return result;
        }

        @ResponseBody
        @RequestMapping(value = "/removeExpenseType", method = RequestMethod.POST, consumes = "application/json")
        public Map<String, Object> removeExpenseType(@RequestBody Map<String,Object> request) {
            Map<String, Object> result = new HashMap<>();
            int id=billService.findtypeidbyname((String) request.get("typename"));
            result.put("code",billService.removeExpenseType(id));
            return result;
        }
    }


