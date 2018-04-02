package cn.edu.nju.software.util;

import cn.edu.nju.software.model.entity.SFBZHNRB;
import org.apache.lucene.document.Document;

public class Convertor {
    public static SFBZHNRB do2SFWSNR(Document document) {
        SFBZHNRB sfbzhnrb = new SFBZHNRB();
        sfbzhnrb.setNR(document.get("wsnr"));
        sfbzhnrb.setBT(document.get("bt"));
//        sfbzhnrb.setBH((int)document.get("bh"));
        return sfbzhnrb;
    }
}
