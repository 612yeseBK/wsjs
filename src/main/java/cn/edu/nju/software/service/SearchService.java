package cn.edu.nju.software.service;

import cn.edu.nju.software.util.Constant;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

@Service("searchService")
public class SearchService {
    public void searchBykeywords(String keywords) throws IOException, InvalidTokenOffsetsException {
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs hits = null;
        ScoreDoc[] scoreDocs = null;
        Query query = null;
        try {
            QueryParser qp = new QueryParser("wsnr", Constant.Analyzer);//用于解析用户输入的工具
            query = qp.parse("原告");
        } catch (ParseException e) {
            // TODO: handle exception
        }
        long startTime = System.currentTimeMillis(); //记录索引开始时间
        TopDocs topDocs = searcher.search(query, 1000);
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("匹配共耗时" + (endTime - startTime) + "毫秒");
        System.out.println("共查询到条目数:" + topDocs.totalHits);
        scoreDocs = topDocs.scoreDocs;

        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>", "</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
        QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分
        Fragmenter fragmenter = new SimpleFragmenter(40);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter); //设置一下要显示的片段

        for (int i = 0; i < scoreDocs.length; i++) {
            int doc1 = scoreDocs[i].doc;
            Document document = searcher.doc(doc1);
            System.out.println("第" + i + "篇");
            System.out.println("编号:" + document.get("bh"));
            System.out.println(document.get("bt"));
            System.out.println(document.get("wsnr"));
            String wsnr = document.get("wsnr");

            TokenStream tokenStream = Constant.Analyzer.tokenStream("desc", new StringReader(wsnr));
            String summary = highlighter.getBestFragment(tokenStream, wsnr);
            System.out.println("高亮之后：" + summary);
        }
        reader.close();
    }


}

