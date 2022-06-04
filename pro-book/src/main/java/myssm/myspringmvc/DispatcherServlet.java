package myssm.myspringmvc;

import myssm.ioc.BeanFactory;
import myssm.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory;

    public DispatcherServlet(){
    }

    public void init() throws ServletException {
        super.init();
        //之前是在此處主動創建IOC容器，現在優化為從application作用域去獲取
        //beanFactory = new ClassPathXmlApplicationContext();
        ServletContext application = getServletContext();
        Object beanFactoryObj = application.getAttribute("beanFactory");
        if(beanFactoryObj != null){
            beanFactory = (BeanFactory) beanFactoryObj;
        } else {
            throw new RuntimeException("IOC容器獲取失敗");
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setCharacterEncoding("UTF-8");

        /*
        * 假設url是: http://localhost:8080/pro03/hello.do
        * 那麼servletPath是: /hello.do
        * 第一步: /hello.do -> hello
        * 第二步: hello -> HelloController
        * */
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = request.getParameter("operate");
        if(StringUtil.isEmpty(operate)){
            operate = "index";
        }

        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for(Method method : methods){
                if(operate.equals(method.getName())){
                    //1. 統一獲取請求參數
                    //1.1 獲取當前方法的參數，返回參數數組
                    Parameter[] parameters = method.getParameters();
                    //1.2 parameterValue用來乘載參數的值
                    Object[] parameterValues = new Object[parameters.length];
                    for(int i = 0;i < parameters.length;i++){
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        //如果參數名是request,response,session，那麼就不是通過請求中獲取參數的方式
                        if("request".equals(parameterName)){
                            parameterValues[i] = request;
                        } else if ("response".equals(parameterName)) {
                            parameterValues[i] = response;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = request.getSession();
                        } else {
                            //從請求中獲取參數值
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue;

                            if(parameterObj != null){
                                if("java.lang.Integer".equals(typeName)){
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }

                            parameterValues[i] = parameterObj;
                        }
                    }

                    //2. controller方法調用
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerBeanObj, parameterValues);

                    //3. 視圖處理
                    String methodReturnStr = (String) returnObj;
                    if(StringUtil.isEmpty(methodReturnStr)) {
                        return ;
                    }
                    if(methodReturnStr.startsWith("redirect:")) {
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        response.sendRedirect(redirectStr);
                    } else if (methodReturnStr.startsWith("json:")) {
//                        response.setCharacterEncoding("utf-8");
//                        response.setContentType("application/json;charset=utf-8");
                        String jsonStr = methodReturnStr.substring("json:".length());
                        PrintWriter out = response.getWriter();
                        out.print(jsonStr);
                        out.flush();
                    } else {
                        super.processTemplate(methodReturnStr, request, response);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("DispatcherServlet出錯");
        }


        //獲取當前類中的所有方法
        //方法二
//        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
//        for(Method m : methods){
//            //獲取方法名
//            String methodName = m.getName();
//            if(operate.equals(methodName)){
//                try {
//                    m.invoke(this, request, response);
//                    return;
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                } catch (InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        throw new RuntimeException("operate值非法!");

        //方法一
//        switch (operate) {
//            case "index":
//                index(request, response);
//                break;
//            case "add":
//                add(request, response);
//                break;
//            case "delete":
//                delete(request, response);
//                break;
//            case "edit":
//                edit(request, response);
//                break;
//            case "update":
//                update(request, response);
//                break;
//            default:
//                throw new RuntimeException("operate值非法!");
//        }
    }
}
