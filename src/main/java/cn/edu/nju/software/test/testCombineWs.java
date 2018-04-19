package cn.edu.nju.software.test;

import cn.edu.nju.software.service.WszhService;
import cn.edu.nju.software.model.dto.SFBZHModel;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class testCombineWs {
    private ApplicationContext ctx = null;
    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void CombineWs(){
        WszhService wszhService = (WszhService) ctx.getBean("wszhService");
        List<SFBZHModel> list =null;
        list = wszhService.findWS();
        System.out.println(list.get(1).getNR());
    }

}
