package cn.edu.nju.software.model.dao;

import cn.edu.nju.software.model.entity.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("personDao")
public class personDao {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    public List<Person> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Person";
        List<Person> l = (List<Person>)session.createQuery(hql).list();
        return l;
    }
}
