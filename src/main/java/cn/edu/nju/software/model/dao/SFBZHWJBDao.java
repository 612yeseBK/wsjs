package cn.edu.nju.software.model.dao;

import cn.edu.nju.software.model.entity.Person;
import cn.edu.nju.software.model.entity.SFBZHNRB;
import cn.edu.nju.software.model.entity.SFBZHWJB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SFBZHWJBDao")
public class SFBZHWJBDao {
    @Autowired
    SessionFactory sessionFactory;

    public List<SFBZHWJB> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from SFBZHWJB";
        List<SFBZHWJB> l = (List<SFBZHWJB>)session.createQuery(hql).list();
        return l;
    }

}
