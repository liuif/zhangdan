package cn.edu.zjut.control;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.service.BillService;
import cn.edu.zjut.service.LedgerService;
import cn.edu.zjut.service.TypeService;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    /**
     * 获取该日账单
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getbillsbydata", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getbillsbydata(@RequestBody Map<String,Object> request) {
        LocalDate date= LocalDate.parse((String) request.get("date"), DateTimeFormatter.ISO_LOCAL_DATE);
        int ledger_id= (int) request.get("ledger_id");
        List<Object[]> lists=billService.selectByDate(date,ledger_id);
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
            bill.put("id",row[4]);
            bill.put("name",row[5]);
            bill.put("date",request.get("date"));
            bills.add(bill);
            System.out.println(bills);
        }
        result.put("data",bills);
        return result;
    }

    /**
     * 添加账单
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/addbill", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> addbill(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=ledgerService.getLedger((Integer) request.get("ledger_id"));
        UserExpenseType userExpenseType=typeService.findtypeidbyname((String) request.get("typename"), (String) request.get("email"));
        User user=userService.getUser((String) request.get("email"));
        Bill bill=new Bill(ledger, Double.parseDouble((String) request.get("money")),userExpenseType,(String) request.get("inorout"),(String) request.get("note"),LocalDate.parse((String)request.get("date")).atStartOfDay(),user);
        if(billService.addOneBill(bill)){
            result.put("code",0);
            return result;
        }else {
            result.put("code",1);
            return result;
        }
    }

    /**
     * 删除账单
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/removeOneBill", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> removeOneBill(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Bill bill=billService.getBill((Integer) request.get("id"));
        if(billService.removeOneBill(bill)){
            result.put("code",1);
            return result;
        }else {
            result.put("code",0);
            return result;
        }
    }

    /**
     * 获取该年每月用户的收支明细
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalIncomeAndExpenseByYear", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getTotalIncomeAndExpenseByYear(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        List Data =new ArrayList<>();
        for(int i=1;i<=12;i++){
            Map<String, Object> data = new HashMap<>();
            data.put("in",billService.INgetTotalIncomeAndExpenseByYear(Integer.parseInt((String) request.get("year")),(String) request.get("email"),i));
            data.put("out",billService.OUTgetTotalIncomeAndExpenseByYear(Integer.parseInt((String) request.get("year")),(String) request.get("email"),i));
            data.put("month",i);
            Data.add(data);
        }
        result.put("code",1);
        result.put("inandout", Data);
        return result;
    }

    /**
     * 获取该年该月每一日用户的收支明细
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalIncomeAndExpenseByMonth", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getTotalIncomeAndExpenseByMonth(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        String date= (String) request.get("date");
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        List Data =new ArrayList<>();
        for(int i=1;i<=31;i++){
            Map<String, Object> data = new HashMap<>();
            data.put("in",billService.INgetTotalIncomeAndExpenseByMonth(year,(String) request.get("email"),month,i));
            data.put("out",billService.OUTgetTotalIncomeAndExpenseByMonth(year,(String) request.get("email"),month,i));
            data.put("day",i);
            Data.add(data);
        }
        result.put("code",1);
        result.put("inandout", Data);
        return result;
    }

    /**
     * 获取该年用户按消费类型分组的收支
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/findDailyIncomeExpenseNetByYear", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> findDailyIncomeExpenseNetByYear(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        List<UserExpenseType> list= typeService.getAllExpenseTypeID((String) request.get("email"));
        List Data1 =new ArrayList<>();
        List Data2 =new ArrayList<>();
        for (UserExpenseType row : list) {
            Map<String, Object> data1 = new HashMap<>();
            Map<String, Object> data2 = new HashMap<>();
            data1.put("value",billService.OUTfindDailyIncomeExpenseNetByYear(Integer.parseInt((String) request.get("year")),row.getType_id(),(String) request.get("email")));
            data1.put("name",row.getType_name());
            Data1.add(data1);
            data2.put("value",billService.INfindDailyIncomeExpenseNetByYear(Integer.parseInt((String) request.get("year")),row.getType_id(),(String) request.get("email")));
            data2.put("name",row.getType_name());
            Data2.add(data2);
        }
        result.put("code",1);
        result.put("out", Data1);
        result.put("in", Data2);
        return result;
    }

    /**
     * 获取该年该月用户按消费类型分组的收支
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/findDailyIncomeExpenseNetByMonth", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> findDailyIncomeExpenseNetByMonth(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        List<UserExpenseType> list= typeService.getAllExpenseTypeID((String) request.get("email"));
        List Data1 =new ArrayList<>();
        List Data2 =new ArrayList<>();
        String date= (String) request.get("date");
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        for (UserExpenseType row : list) {
            Map<String, Object> data1 = new HashMap<>();
            data1.put("value",billService.OUTfindDailyIncomeExpenseNetByMonth(year,month,row.getType_id(),(String) request.get("email")));
            data1.put("name",row.getType_name());
            Data1.add(data1);
            Map<String, Object> data2 = new HashMap<>();
            data2.put("value",billService.INfindDailyIncomeExpenseNetByMonth(year,month,row.getType_id(),(String) request.get("email")));
            data2.put("name",row.getType_name());
            Data2.add(data2);
        }
        result.put("code",1);
        result.put("out", Data1);
        result.put("in", Data2);
        return result;
    }

    /**
     * 获取该用户一年的总支出
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/CountAllbyYear", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> CountAllbyYear(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Double all=billService.CountAllbyYear(Integer.parseInt((String) request.get("year")), (Integer) request.get("ledger_id"));
        result.put("value",all);
        result.put("code",1);
        return result;
    }

    /**
     * 修改账单
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/UpdateBill", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> UpdateBill(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=ledgerService.getLedger((Integer) request.get("ledger_id"));
        UserExpenseType userExpenseType=typeService.findtypeidbyname((String) request.get("typename"), (String) request.get("email"));
        User user=userService.getUser((String) request.get("email"));
        Bill bill=new Bill(ledger, Double.parseDouble((String) request.get("money")),userExpenseType,(String) request.get("inorout"),(String) request.get("note"),LocalDate.parse((String)request.get("date")).atStartOfDay(),user);
        bill.setBillId((Integer) request.get("id"));
        if(billService.updateOneBill(bill)){
            result.put("code",0);
            return result;
        }else {
            result.put("code",1);
            return result;
        }
    }
}


