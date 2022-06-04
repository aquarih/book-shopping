package z_book.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = {"*.do","*.html"},
        initParams = {
            @WebInitParam(name = "whitelist", value = "/pro09/page.do?operate=page&page=user/login,/pro09/user.do?null,/pro09/page.do?operate=page&page=user/regist")
        })
public class SessionFilter implements Filter {
    List<String> whiteList = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String white = filterConfig.getInitParameter("whitelist");
        String[] whiteArr = white.split(",");
        whiteList = Arrays.asList(whiteArr);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String str = uri + "?" + queryString;

        if (whiteList.contains(str)) {
            filterChain.doFilter(request, response);
        } else {
            Object currentUserObj = session.getAttribute("currentUser");
            if (currentUserObj == null) {
                response.sendRedirect("page.do?operate=page&page=user/login");
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
