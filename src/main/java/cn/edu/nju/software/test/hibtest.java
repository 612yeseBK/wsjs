package cn.edu.nju.software.test;

import cn.edu.nju.software.model.entity.Person;
import cn.edu.nju.software.service.ExampleService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

public class hibtest {

    private ApplicationContext ctx = null;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    /**
     * 测试数据库连接
      */
    @Test
    public void testDataSource() throws SQLException {
        System.out.println("数据源:" + ctx);
        DataSource dataSource = (DataSource)ctx.getBean("dataSource");
        System.out.println("数据库连接:" + dataSource.getConnection().toString());
        SessionFactory sessionFactory = (SessionFactory)ctx.getBean("sessionFactory");
        System.out.println("sessionfactory:" + sessionFactory);
//        Connection conn = dataSource.getConnection();
//        Statement sst=conn.createStatement();
////        PreparedStatement st = conn.prepareStatement("select * from c_attachment");
//        ResultSet rs=sst.executeQuery("select * from c_attachment");
//        while(rs.next()){
//            System.out.println(rs.getString("id")+" "
//                    +rs.getString("name"));
//        }
//        rs.close();
//        sst.close();
//        conn.close();
    }

    /**
     * 测试sessionFactory
     * @throws SQLException
     */
    @Test
    public void testSessionFactory() throws SQLException {
        SessionFactory sessionFactory = (SessionFactory)ctx.getBean("sessionFactory");
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new Person("李四",22));
        session.getTransaction().commit();
    }

    /**
     * 测试基于注解的声明式事务
     * @throws SQLException
     */
    @Test
    public void testService() throws SQLException {
        ExampleService ExampleService =  (ExampleService) ctx.getBean("exampleService");
        ExampleService.savePerson();
    }

}
