package cn.edu.nju.software.model.dto;

import java.util.Date;

public class SFBZHModel {

    private int WJBH;   //文件编号

    private String NR;  //内容

    private String BZHWJLX;//标准化文件类型
    private String BZHWJMC;//标准化文件名称
    private String ZCR;//主持人
    private String CBDW;//承办单位
    private String XBDW;//协办单位
    private String FBDW;//发布单位
    private Date FBSJ;//发布时间
    private String BBH;//版本号
    private String LLR;//联络人
    private String BZHWJZMC;//标准化文件子名称
    private Date XDSJ;//修订时间

    public int getWJBH() {
        return WJBH;
    }

    public void setWJBH(int WJBH) {
        this.WJBH = WJBH;
    }

    public String getNR() {
        return NR;
    }

    public void setNR(String NR) {
        this.NR = NR;
    }

    public String getBZHWJLX() {
        return BZHWJLX;
    }

    public void setBZHWJLX(String BZHWJLX) {
        this.BZHWJLX = BZHWJLX;
    }

    public String getBZHWJMC() {
        return BZHWJMC;
    }

    public void setBZHWJMC(String BZHWJMC) {
        this.BZHWJMC = BZHWJMC;
    }

    public String getZCR() {
        return ZCR;
    }

    public void setZCR(String ZCR) {
        this.ZCR = ZCR;
    }

    public String getCBDW() {
        return CBDW;
    }

    public void setCBDW(String CBDW) {
        this.CBDW = CBDW;
    }

    public String getXBDW() {
        return XBDW;
    }

    public void setXBDW(String XBDW) {
        this.XBDW = XBDW;
    }

    public String getFBDW() {
        return FBDW;
    }

    public void setFBDW(String FBDW) {
        this.FBDW = FBDW;
    }

    public Date getFBSJ() {
        return FBSJ;
    }

    public void setFBSJ(Date FBSJ) {
        this.FBSJ = FBSJ;
    }

    public String getBBH() {
        return BBH;
    }

    public void setBBH(String BBH) {
        this.BBH = BBH;
    }

    public String getLLR() {
        return LLR;
    }

    public void setLLR(String LLR) {
        this.LLR = LLR;
    }

    public String getBZHWJZMC() {
        return BZHWJZMC;
    }

    public void setBZHWJZMC(String BZHWJZMC) {
        this.BZHWJZMC = BZHWJZMC;
    }

    public Date getXDSJ() {
        return XDSJ;
    }

    public void setXDSJ(Date XDSJ) {
        this.XDSJ = XDSJ;
    }


}
