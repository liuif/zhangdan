package cn.edu.zjut.control;

import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.Budget;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.UserExpenseType;
import cn.edu.zjut.service.BudgetService;
import cn.edu.zjut.service.LedgerService;
import cn.edu.zjut.service.SharedService;
import cn.edu.zjut.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 13259
 */
@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private SharedService sharedService;

    /**
     * 获取总预算
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalBudget", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> updateTotalBudget(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        List<Ledger> lists1=sharedService.getAllLedger((String) request.get("email"));
        List<Ledger> lists2=ledgerService.getAllLedger((String) request.get("email"));
        List<Object> ledgers1 = new ArrayList<>();;
        List<Object> ledgers2 = new ArrayList<>();;
        result.put("code", 0);
        result.put("msg", "查找成功");
        System.out.println("00000000");
        result.put("ledger_id", lists2.get(0).getLedgerId());
        System.out.println("111111111");
        for (Ledger row : lists2) {
            Map<String, Object> ledger = new HashMap<>();
            ledger.put("ledgername",row.getLedgerName());
            ledger.put("ledgerid",row.getLedgerId());
            ledger.put("budget",row.getBudget());
            ledger.put("currentbudget",row.getCurrent_budget());
            ledgers1.add(ledger);
            System.out.println(row.getLedgerName());
        }
        for (Ledger row : lists1) {
            Map<String, Object> ledger = new HashMap<>();
            ledger.put("ledgername","共享:"+row.getLedgerName());
            ledger.put("ledgerid",row.getLedgerId());
            ledger.put("budget",row.getBudget());
            ledger.put("currentbudget",row.getCurrent_budget());
            ledgers2.add(ledger);
            System.out.println(row.getLedgerName());
        }
        ledgers1.addAll(ledgers2);
        result.put("ledger",(ledgers1));
        return result;
    }

    /**
     * 获取分预算
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/getBudgetDetails", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBudgetDetails(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        String date= (String) request.get("period");
        DateTimeFormatter formatter;
        if(date.length()==7) {
            formatter = DateTimeFormatter.ofPattern("yyyy-M月");
        }else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM月");
        }
        LocalDate period = YearMonth.parse(date, formatter).atDay(1);
        List<Object[]> budgets=budgetService.getTotalBudget((Integer) request.get("ledger_id"),period);
        List list=new ArrayList<>();
        result.put("code", 0);
        result.put("msg", "查找成功");
        for (Object[] row : budgets) {
            Map<String, Object> budget = new HashMap<>();
            budget.put("typename",row[0]);
            budget.put("typeid",row[1]);
            budget.put("budget",row[2]);
            budget.put("currentbudget",row[3]);
            list.add(budget);
        }
        result.put("data",(list));
        return result;
    }

    /**
     * 添加分预算
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/updateBudgetDetails", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> updateBudgetDetails(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=ledgerService.getLedger((Integer) request.get("ledger_id"));
        Budget budget=new Budget();
        budget.setLedger(ledger);
        String date= (String) request.get("period");
        DateTimeFormatter formatter;
        if(date.length()==7) {
            formatter = DateTimeFormatter.ofPattern("yyyy-M月");
        }else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM月");
        }
        LocalDateTime period = YearMonth.parse(date, formatter).atDay(1).atStartOfDay();
        budget.setPeriod(period);
        budget.setBudget(Double.parseDouble((String) request.get("budget")));
        budget.setCurrentBudget( Double.parseDouble((String) request.get("budget")));
        UserExpenseType userExpenseType=typeService.findtypeidbyname((String) request.get("typename"), (String) request.get("email"));
        budget.setuserexpensetype(userExpenseType);
        if(budgetService.updateBudgetDetails(budget)){
            budgetService.updateTotalBudget(budget.getLedger(),budgetService.getBudget(ledger, LocalDate.from(period)));
            result.put("code",0);
            return result;
        }else {
            result.put("code",1);
            return result;
        }
    }

    /**
     * 判断是否超支
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/notifyBudgetExceedance", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> notifyBudgetExceedance(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        Ledger ledger=ledgerService.getLedger((Integer) request.get("ledger_id"));
        if(ledger.getCurrent_budget()<ledger.getBudget()*0.1){
            result.put("code",1);
            return result;
        }else {
            result.put("code",0);
            return result;
        }
    }

    /**
     * 删除预算
     * @param request
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/removeBudget", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> removeBudget(@RequestBody Map<String,Object> request) {
        Map<String, Object> result = new HashMap<>();
        int ledger_id= (int) request.get("ledger_id");
        int type_id= (int) request.get("type_id");
        Ledger ledger=ledgerService.getLedger(ledger_id);
        String date= (String) request.get("period");
        DateTimeFormatter formatter;
        if(date.length()==7) {
            formatter = DateTimeFormatter.ofPattern("yyyy-M月");
        }else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM月");
        }
        LocalDate period = YearMonth.parse(date, formatter).atDay(1);
        if(budgetService.removeBudget(ledger_id,type_id,period)){
            budgetService.updateTotalBudget(ledger,budgetService.getBudget(ledger, LocalDate.from(period)));
            result.put("code",0);
            return result;
        }else {
            result.put("code",1);
            return result;
        }
    }
}


