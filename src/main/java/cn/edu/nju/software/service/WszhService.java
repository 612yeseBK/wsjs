package cn.edu.nju.software.service;

import cn.edu.nju.software.model.dao.SFBZHNRBDao;
import cn.edu.nju.software.model.dao.SFBZHWJBDao;
import cn.edu.nju.software.model.entity.SFBZHNRB;
import cn.edu.nju.software.model.entity.SFBZHWJB;
import cn.edu.nju.software.model.dto.SFBZHModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("wszhService")
public class WszhService {
    private static Logger log = Logger.getLogger(WszhService.class);

    @Autowired
    private SFBZHNRBDao sfbzhnrbDao;
    @Autowired
    private SFBZHWJBDao sfbzhwjbDao;

    @Transactional
    public List<SFBZHModel> findWS(){
        long start = System.currentTimeMillis();
        List<SFBZHModel> sfbzhModelList = new ArrayList<>();
        List<SFBZHWJB> list = sfbzhwjbDao.findWjb();
        if(list != null){
            for(int i = 0; i < list.size(); i++){
                SFBZHModel sfbzhModel = new SFBZHModel();
                SFBZHWJB sfbzhwjb = list.get(i);
                int bh = sfbzhwjb.getBH();
                List<SFBZHNRB> NrbList = sfbzhnrbDao.findNR(bh);
                if(NrbList != null && !NrbList.isEmpty()){
                    String nr = "";
                    for(SFBZHNRB sfbzhnrb : NrbList){
                        nr += sfbzhnrb.getBT() + '\n';
                        nr += sfbzhnrb.getNR() + '\n';
                    }
                    sfbzhModel.setNR(nr);
                }
                sfbzhModel.setWJBH(sfbzhwjb.getBH());
                sfbzhModel.setBZHWJLX(sfbzhwjb.getBZHWJLX());
                sfbzhModel.setBZHWJMC(sfbzhwjb.getBZHWJMC());
                sfbzhModel.setZCR(sfbzhwjb.getZCR());
                sfbzhModel.setCBDW(sfbzhwjb.getCBDW());
                sfbzhModel.setXBDW(sfbzhwjb.getXBDW());
                sfbzhModel.setFBDW(sfbzhwjb.getFBDW());
                sfbzhModel.setFBSJ(sfbzhwjb.getFBSJ());
                sfbzhModel.setBBH(sfbzhwjb.getBBH());
                sfbzhModel.setLLR(sfbzhwjb.getLLR());
                sfbzhModel.setBZHWJZMC(sfbzhwjb.getBZHWJZMC());
                sfbzhModel.setXDSJ(sfbzhwjb.getXDSJ());
                sfbzhModelList.add(sfbzhModel);
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("组合对象耗时：" + (end - start) + "ms");
        return sfbzhModelList;
    }

    /**
     * 根据文件编号从文书内容表里获取文书内容，并分段存储
     * @param wjbh 文件编号
     * @return map里面是文件小标题作为key，段落内容作为value，返回前端展示
     */
    @Transactional
    public List<Map> findContentByWjbh(int wjbh){
        List<Map> list = new ArrayList<>();
        List<SFBZHNRB> NrbList = sfbzhnrbDao.findNR(wjbh);
        if(NrbList != null && !NrbList.isEmpty()){
            for(SFBZHNRB sfbzhnrb : NrbList){
                Map map = new HashMap();
                log.info(sfbzhnrb.getBT());
                log.info(sfbzhnrb.getNR());
                map.put(sfbzhnrb.getBT(),sfbzhnrb.getNR());
                list.add(map);
            }
        }
        return list;
    }
}
