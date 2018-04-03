package cn.edu.nju.software.test;

import cn.edu.nju.software.util.updateTask;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class testTimer {


    public static void main(String[] args){
        Timer timer;
        Calendar calendar =Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        //定制每天的1:00:00执行
        calendar.set(year, month, day, 01, 00, 00);
        Date date = calendar.getTime();
        System.out.println(date);
        int period = 6 * 1000;
        timer = new Timer("lucene更新");
        timer.schedule(new updateTask(),date, period);//每天一点开始，每隔24小时执行该任务
    }
}
