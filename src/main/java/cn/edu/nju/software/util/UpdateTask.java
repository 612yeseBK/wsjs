package cn.edu.nju.software.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.TimerTask;

/**
 * description:用以在执行索引更新的定时器任务
 * Created by gaoyw on 2018/4/7.
 */
public class UpdateTask extends TimerTask{
    private boolean isRunning = false;
    private ApplicationContext app;
    private ServletContext servletContext = null;
    public UpdateTask(){}
    public UpdateTask(ServletContext servletContext){
        this.servletContext = servletContext;
    }
    @Override
    public void run() {
//        可用于获取ApplicationContext,进行后续操作，暂时放着
//        app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//        app.getBean("searchService");
        if (!isRunning) {
            isRunning = true;
            System.out.println("开始执行更新索引库任务..."); //开始任务
            System.out.println(new Date());
            //TODO  需要执行任务的代码
            System.out.println("执行任务完成..."); //任务完成
            isRunning = false;
        } else {
            System.out.println("上一次任务执行还未结束..."); //上一次任务执行还未结束
        }
    }
}
