package cn.edu.nju.software.model.dao;

import cn.edu.nju.software.model.entity.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("SFBZHWJBDao")
public class SFBZHWJBDao {
    @Autowired
    SessionFactory sessionFactory;

    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

}
