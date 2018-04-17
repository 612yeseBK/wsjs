package cn.edu.nju.software.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * description:分词和lucene的一些位置变量
 * Created by gaoyw on 2018/4/4.
 */
public class Constant {
//    public static final String IndexPath = "C:\\Users\\gyong\\Desktop\\fyxmz\\wsjs\\index";
//    public static final String cl = "/Users/zhupeng/IdeaProjects/wsjs";
    public static String IndexPath = "C:\\Users\\gyong\\Desktop\\fyxmz\\wsjs\\index"; //索引文件
    public static String AddIndexPath = "C:\\Users\\gyong\\Desktop\\fyxmz\\wsjs\\indexAdd"; //索引文件
    public static Analyzer Analyzer=new IKAnalyzer();
    public static final int LengthOfBack = 100;

    public final static String Index_Wsnr = "wsnr"; //文书内容
    public final static String Index_Zcr = "zcr"; //主持人
    public final static String Index_Llr = "llr"; //联络人
    public final static String Index_Wjbh = "wjbh"; //文件编号
    public final static String Index_Bzhwjmc = "bzhwjmc"; //标准化文件名称
    public final static String Index_Cbdw = "cbdw"; //承办单位
    public final static String Index_Xbdw = "xbdw"; //协办单位


    //
    public final static int diff = 1;

    // 获取可以排序的field类型
    public static FieldType Int_FIELD_TYPE_STORED_SORTED = new FieldType(IntField.TYPE_STORED);
    static {
        Int_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
        Int_FIELD_TYPE_STORED_SORTED.freeze();
    }
}
