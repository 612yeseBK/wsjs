package cn.edu.nju.software.util;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.TimerTask;

public class updateTask extends TimerTask{
    private boolean isRunning = false;
    private ServletContext servletContext = null;
    public updateTask(){}
    public updateTask(ServletContext servletContext){
        this.servletContext = servletContext;
    }
    @Override
    public void run() {
        if (!isRunning) {
            isRunning = true;
            System.out.println("开始执行任务..."); //开始任务
            System.out.println(new Date());
            //TODO  需要执行任务的代码
            System.out.println("执行任务完成..."); //任务完成
            isRunning = false;
        } else {
            System.out.println("上一次任务执行还未结束..."); //上一次任务执行还未结束
        }
    }
}
