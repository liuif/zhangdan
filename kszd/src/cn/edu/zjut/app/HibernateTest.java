package cn.edu.zjut.app;


import cn.edu.zjut.po.Bill;
import cn.edu.zjut.po.ExpenseType;
import cn.edu.zjut.po.Ledger;
import cn.edu.zjut.po.User;
import cn.edu.zjut.service.BillService;
import cn.edu.zjut.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HibernateTest {
    public static void main(String[] args) {
        UserService userService=new UserService();
        User user=new User();
        user.setPassword("password");
        user.setEmail("email");
//        if (userService.login(user)) {
//            System.out.println("user success");
//        } else {
//            System.out.println("user Invalid username or password");
//        }
//        System.out.println("///");
//        User user1=new User();
////        user1.setPassword("pass");
////        user1.setEmail("email");
//        if (userService.login(user)) {
//            System.out.println("user1 success");
//            System.out.println(userService.getname(user.getEmail()));
//        }
//        else {
//            System.out.println("user1 Invalid username or password");
//        }
////        System.out.println("///");
//        User user2=new User();
//        user2.setPassword("password");
//        user2.setEmail("email2");
//        user2.setName("qnm");
//        if (userService.register(user2)) {
//            System.out.println("success");
//        } else {
//            System.out.println("Invalid username or password");
//        }

//        Ledger ledger=new Ledger();
//        ledger.setLedgerId(1);
//        ledger.setUser(user);
//        Bill bill=new Bill();
//        BillService billService = new BillService();
//        bill.setBillId(2);
//        bill.setLedger(ledger);
//        bill.setMoney(10.0F);
//        ExpenseType expenseType=new ExpenseType();
//        expenseType.setTypeId(1);
//        expenseType.setTypeName("餐饮美食");
//        bill.setExpenseType(expenseType);
//        bill.setIncomeOrExpense("in");
//        bill.setNote("note");
//        bill.setBillCreateDate(LocalDateTime.now());
//
//
//
        String email="email";
        String dta="2023-12-13";
        LocalDate data= LocalDate.parse(dta);
        BillService billService = new BillService();
        List<Object[]> lists=billService.selectByDate(data,email);
        System.out.println(lists);
        System.out.println(lists.size());
        for (Object[] row : lists) {
            System.out.println("/");
            System.out.println(row[0]);
            System.out.println(row[1]);
            System.out.println(row[2]);
            System.out.println(row[3]);
        }

//        List<Object[]> lists=billService.selectByType(expenseType.getTypeName());
//        for (Object[] row : lists) {
//            System.out.println("/");
//            System.out.println(row[0]);
//            System.out.println(row[1]);
//            System.out.println(row[2]);
//            System.out.println(row[3]);
//            System.out.println(row[4]);
//        }
//
//        System.out.println("///");
//
//        if(billService.addOneBill(bill))
//            System.out.println("bill add success");
//        else
//            System.out.println("bill add fail");
//
//        System.out.println("///");
//        bill.setBillId(10);
//        if(billService.updateOneBill(bill))
//            System.out.println("bill update success");
//        else
//            System.out.println("bill update fail");

//        System.out.println("///");
//        bill.setBillId(2);
//        billService.addOneBill(bill);
//        bill.setBillId(10);
//        bill.setIncomeOrExpense("out");
//        if(billService.removeOneBill(bill))
//            System.out.println("bill remove success");
//        else
//            System.out.println("bill remove fail");


    }
//
}