package cn.edu.nju.software.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sfbzh_wjb")
public class Sfbzhwjb {
    /**
     * 	只需要具有搜索功能即可，所以就不指定别的信息
     */
    private int BH;//编号
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

    @Id
    @Column(name = "BH", nullable = false, unique = true, length = 1)
    public int getBH() {
        return BH;
    }

    public void setBH(int BH) {
        this.BH = BH;
    }

    @Column(name="BZHWJLX")
    public String getBZHWJLX() {
        return BZHWJLX;
    }

    public void setBZHWJLX(String BZHWJLX) {
        this.BZHWJLX = BZHWJLX;
    }

    @Column(name="BZHWJMC")
    public String getBZHWJMC() {
        return BZHWJMC;
    }

    public void setBZHWJMC(String BZHWJMC) {
        this.BZHWJMC = BZHWJMC;
    }

    @Column(name="ZCR")
    public String getZCR() {
        return ZCR;
    }

    public void setZCR(String ZCR) {
        this.ZCR = ZCR;
    }

    @Column(name="CBDW")
    public String getCBDW() {
        return CBDW;
    }

    public void setCBDW(String CBDW) {
        this.CBDW = CBDW;
    }

    @Column(name="XBDW")
    public String getXBDW() {
        return XBDW;
    }

    public void setXBDW(String XBDW) {
        this.XBDW = XBDW;
    }

    @Column(name="FBDW")
    public String getFBDW() {
        return FBDW;
    }

    public void setFBDW(String FBDW) {
        this.FBDW = FBDW;
    }

    @Column(name="FBSJ")
    public Date getFBSJ() {
        return FBSJ;
    }

    public void setFBSJ(Date FBSJ) {
        this.FBSJ = FBSJ;
    }

    @Column(name="BBH")
    public String getBBH() {
        return BBH;
    }

    public void setBBH(String BBH) {
        this.BBH = BBH;
    }

    @Column(name="LLR")
    public String getLLR() {
        return LLR;
    }

    public void setLLR(String LLR) {
        this.LLR = LLR;
    }

    @Column(name="BZHWJZMC")
    public String getBZHWJZMC() {
        return BZHWJZMC;
    }

    public void setBZHWJZMC(String BZHWJZMC) {
        this.BZHWJZMC = BZHWJZMC;
    }

    @Column(name="XDSJ")
    public Date getXDSJ() {
        return XDSJ;
    }

    public void setXDSJ(Date XDSJ) {
        this.XDSJ = XDSJ;
    }



}
