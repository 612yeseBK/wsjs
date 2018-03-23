package cn.edu.nju.software.service;

import cn.edu.nju.software.model.dao.personDao;
import cn.edu.nju.software.model.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("exampleService")
public class exampleService {
    @Autowired
    personDao personDao;

    @Transactional
    public void savePerson() {
        personDao.save(new Person("麻子",22));
    }
}
