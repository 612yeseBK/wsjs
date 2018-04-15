package cn.edu.nju.software.util;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * description:分词和lucene的一些位置变量
 * Created by gaoyw on 2018/4/4.
 */
public class Constant {
    public static final String IndexPath = "C:\\Users\\gyong\\Desktop\\fyxmz\\wsjs\\index";
    public static Analyzer Analyzer=new IKAnalyzer();
}
