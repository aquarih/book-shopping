package book.controller;

import book.pojo.Cart;
import book.pojo.User;
import book.service.CartItemService;
import book.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserController {
    private UserService userService;
    private CartItemService cartItemService;

    public String login(String uname, String pwd, HttpSession session) {
        User user = userService.login(uname, pwd);
        if(user != null) {
            Cart cart = cartItemService.getCart(user);
            user.setCart(cart);

            session.setAttribute("currentUser", user);
            return "redirect:book.do";
        }

        return "user/login";
    }

    public String regist(String uname, String pwd, String email, String verifyCode, HttpSession session, HttpServletResponse response) throws IOException {
        Object kaptchaCodeObj = session.getAttribute("KAPTCHA_SESSION_KEY");
        if (kaptchaCodeObj == null || !verifyCode.equals(kaptchaCodeObj)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script language='javascript'>alert('驗證碼不正確!');window.location.href='page.do?operate=page&page=user/regist';</script>");
//            return "user/regist";
            return "";
        } else {
            if (verifyCode.equals(kaptchaCodeObj)) {
                User user = new User(uname, pwd, email, 0);
                userService.regist(user);
            }
        }

        return "user/login";
    }
}
