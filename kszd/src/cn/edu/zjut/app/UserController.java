package cn.edu.zjut.app;

import cn.edu.zjut.po.User;
import cn.edu.zjut.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
                               @RequestParam("password") String password,
                               @RequestParam("email") String email,
                               Model model) {
        UserService userService=new UserService();
        User user=new User();
        user.setPassword(password);
        user.setEmail(email);
        if (userService.login(user)) {
            model.addAttribute("email", email);
            return "welcome";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      Model model) {
        UserService userService=new UserService();
        User user=new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        if(userService.register(user)){
            model.addAttribute("username", username);
            return "welcome";
        }else {
            model.addAttribute("error", "register failed");
            return "register";
        }
    }
}