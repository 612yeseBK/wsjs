package cn.edu.nju.software.model.dao;

import cn.edu.nju.software.model.entity.SFBZHNRB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("SFBZHNRBDao")
public class SFBZHNRBDao {
    @Autowired
    SessionFactory sessionFactory;

    public List<SFBZHNRB> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from SFBZHNRB";
        List<SFBZHNRB> l = (List<SFBZHNRB>)session.createQuery(hql).list();
        return l;
    }

}
