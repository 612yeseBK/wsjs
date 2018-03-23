package cn.edu.nju.software.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sfbzh_nrb")
public class SFBZHNRB {
    /**
     * 	只需要具有搜索功能即可，所以就不指定别的信息
     */
    private int BH;     //编号
    private int WJBH;   //文件编号
    private int XH;     //序号
    private String BT;  //标题

    @Id
    @Column(name = "BH", nullable = false, unique = true, length = 1)
    public int getBH() {
        return BH;
    }

    public void setBH(int BH) {
        this.BH = BH;
    }

    public int getWJBH() {
        return WJBH;
    }

    public void setWJBH(int WJBH) {
        this.WJBH = WJBH;
    }

    public int getXH() {
        return XH;
    }

    public void setXH(int XH) {
        this.XH = XH;
    }

    public String getBT() {
        return BT;
    }

    public void setBT(String BT) {
        this.BT = BT;
    }

    public String getNR() {
        return NR;
    }

    public void setNR(String NR) {
        this.NR = NR;
    }

    private String NR;  //内容

}
