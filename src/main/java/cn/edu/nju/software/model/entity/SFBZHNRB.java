package cn.edu.nju.software.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "sfbzh_nrb")
public class SFBZHNRB {

    /**
     * 	文件编号是指用来说明这条记录是属于哪一个文件的，
     * 	序号是指在同一个文件编号下，该文件各部分内容的组织顺序
     * 	标题是指文件的某一部分的小标题
     * 	内容是该段落的内容
     */
    private int BH;     //编号
    private int WJBH;   //文件编号
    private int XH;     //序号
    private String BT;  //标题
    private String NR;  //内容

    @Id
    @Column(name = "BH", nullable = false, unique = true)
    public int getBH() {
        return BH;
    }

    public void setBH(int BH) {
        this.BH = BH;
    }

    @Column(name = "WJBH")
    public int getWJBH() {
        return WJBH;
    }

    public void setWJBH(int WJBH) {
        this.WJBH = WJBH;
    }

    @Column(name = "XH")
    public int getXH() {
        return XH;
    }

    public void setXH(int XH) {
        this.XH = XH;
    }

    @Column(name = "BT")
    public String getBT() {
        return BT;
    }

    public void setBT(String BT) {
        this.BT = BT;
    }

    @Column(name = "NR")
    public String getNR() {
        return NR;
    }

    public void setNR(String NR) {
        this.NR = NR;
    }

}
