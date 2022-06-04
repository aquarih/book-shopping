package myssm.listeners;

import myssm.ioc.BeanFactory;
import myssm.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//監聽上下文啟動，在上下文啟動時候去創建IOC容器，然後將其保存到application作用域
//後面中央控制器再從application作用域中獲取IOC容器
@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1. 獲取Servlet上下文
        ServletContext application = servletContextEvent.getServletContext();
        //2. 獲取上下文的初始化參數
        String path = application.getInitParameter("contextConfigLocation");
        //3. 創建IOC容器
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(path);
        //4. 將IOC容器保存到application作用域
        application.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
