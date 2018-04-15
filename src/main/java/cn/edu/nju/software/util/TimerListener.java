package cn.edu.nju.software.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * description:注册到监听器，执行更新索引库的任务
 * Created by gaoyw on 2018/4/15.
 */
public class TimerListener implements ServletContextListener{

    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //设置执行时间
        Calendar calendar =Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        //定制每天的1:00:00执行
        calendar.set(year, month, day, 01, 00, 00);
        Date date = calendar.getTime();
        System.out.println(date);
        int period = 24 * 60 * 60 * 1000;
        timer = new Timer("lucene更新",true);
        timer.schedule(new UpdateTask(sce.getServletContext()),date, period);//每天一点开始，每隔24小时执行该任务
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
    }
}
