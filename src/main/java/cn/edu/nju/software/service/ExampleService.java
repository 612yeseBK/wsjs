package cn.edu.nju.software.service;

import cn.edu.nju.software.model.dao.SFBZHNRBDao;
import cn.edu.nju.software.model.dao.personDao;
import cn.edu.nju.software.model.entity.Person;
import cn.edu.nju.software.model.entity.SFBZHNRB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("exampleService")
public class ExampleService {
    @Autowired
    personDao personDao;
    @Autowired
    SFBZHNRBDao sfbzhnrbDao;

    @Transactional
    public void savePerson() {
        personDao.save(new Person("麻子",22));
    }

    @Transactional
    public List<SFBZHNRB> findAllSFBZHNR() {
        List<SFBZHNRB> l = sfbzhnrbDao.findAll();
        return l;
    }
}
