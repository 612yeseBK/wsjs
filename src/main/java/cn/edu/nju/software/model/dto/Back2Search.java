package cn.edu.nju.software.model.dto;
import org.apache.lucene.document.*;

/**
 * description:搜索后用于返回前端的对象
 * Created by gaoyw on 2018/4/8.
 */
public class Back2Search {
    private String wjbh;
    private int docid;
    private String bt;
    private String info;
    private String HLContent;

    public Back2Search() {}
    public Back2Search(Document document){
        this.wjbh = document.get("wjbh");
//        this.bt = document.get("bt");
        this.bt = "这里展示标题";
        this.info = "发布时间：2015年12月16日  主持人：xxx  主持单位：xxxx高院";
        this.HLContent = document.get("wsnr");
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }
    public String getWjbh() {
        return wjbh;
    }

    public void setWjbh(String wjbh) {
        this.wjbh = wjbh;
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
        return "id:"+this.wjbh+"\n标题："+this.bt+"\n信息："+this.info+"\n高亮："+this.HLContent;
    }
}
