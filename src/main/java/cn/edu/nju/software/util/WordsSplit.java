package cn.edu.nju.software.util;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * description:使用IK分词工具
 * Created by gaoyw on 2018/4/6.
 */
public class WordsSplit {

    private static Logger log = Logger.getLogger(WordsSplit.class);

    /**
     * 将输入的句子进行分词，返回分词之后的列表
     * @param sentence 输入的句子
     * @return
     */
    public static List<String> getWords(String sentence) {
        String keyword = sentence;
        List<String> tokens = new ArrayList<String>();
        if (StringUtil.isBlank(keyword)) {
            return tokens;
        }
        try {
            Analyzer anal = Constant.Analyzer;
            StringReader reader = new StringReader(keyword);
            TokenStream ts = anal.tokenStream("", reader);
            ts.reset();
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            while (ts.incrementToken()) {
                tokens.add(term.toString());
            }
            reader.close();
            ts.close();
        } catch (IOException e) {
            e.printStackTrace();
            tokens.clear();
            tokens.add(keyword);
        }
//        log.info("分词结果为：" + tokens);
        return tokens;
    }
}
