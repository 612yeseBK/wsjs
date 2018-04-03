package cn.edu.nju.software.model.dto;
import org.apache.lucene.document.*;

public class Back2Search {
    // 搜索后，用于返回给前端的对象
    private String id;
    private String bt;
    private String info;
    private String HLContent;

    public Back2Search() {}
    public Back2Search(Document document){
        this.id = document.get("id");
        this.bt = document.get("bt");
        this.HLContent = document.get("wsnr");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHLContent() {
        return HLContent;
    }

    public void setHLContent(String HLContent) {
        this.HLContent = HLContent;
    }

    @Override
    public String toString(){
        return "id:"+this.id+"\n标题："+this.bt+"\n信息："+this.info+"\n高亮："+this.HLContent;
    }
}
