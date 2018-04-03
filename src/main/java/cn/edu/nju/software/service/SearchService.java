package cn.edu.nju.software.service;

import cn.edu.nju.software.model.dto.Back2Search;
import cn.edu.nju.software.test.testluncene;
import cn.edu.nju.software.util.Constant;
import cn.edu.nju.software.util.WordsSplit;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service("searchService")
public class SearchService {
    private static Logger log = Logger.getLogger(SearchService.class);
    public List<Back2Search> searchByKeywords(String keywords) throws IOException, InvalidTokenOffsetsException {
        List<String> splitedWords = WordsSplit.getWords(keywords);
        BooleanQuery booleanQuery = getBQFormWords("wsnr",splitedWords);
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        long startTime = System.currentTimeMillis(); //记录索引开始时间
        TopDocs topDocs = searcher.search(booleanQuery, 1000);//检索过程
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("匹配共耗时" + (endTime - startTime) + "毫秒");
        System.out.println("共查询到条目数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //高亮设置
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>", "</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
        QueryScorer scorer = new QueryScorer(booleanQuery);//计算得分，会初始化一个查询结果最高的得分
        Fragmenter fragmenter = new SimpleFragmenter(100);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter); //设置一下要显示的片段
        List<Back2Search> back2SearchList = new ArrayList<>();
        // 分析处理检索内容
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc1 = scoreDocs[i].doc;//类似于数据库记录的id
            Document document = searcher.doc(doc1);//根据id去获得lucene的数据库
            Back2Search back2Search = new Back2Search(document);// 建立用于返回前端的对象
            String wsnr = document.get("wsnr");
            TokenStream tokenStream = Constant.Analyzer.tokenStream("wsnr", new StringReader(wsnr));
            String summary = highlighter.getBestFragment(tokenStream, wsnr);
            back2Search.setHLContent(summary);
            log.info(back2Search);
            back2SearchList.add(back2Search);
        }
        reader.close();
        return back2SearchList;
    }

    /**
     * 将搜索的关键字和对应的field结合成一个query
     * @param field lucene的域
     * @param keywords 搜索的关键字 关联关系 与（MUST)
     * @return
     */
    public BooleanQuery getBQFormWords(String field, List<String> keywords) {
        BooleanQuery query = new BooleanQuery();
        for (String keyword : keywords) {
            Term term = new Term(field, keyword);
            TermQuery tQuery = new TermQuery(term);
            BooleanClause clause = new BooleanClause(tQuery,
                    BooleanClause.Occur.MUST);
            query.add(clause);
            // api里两种写法：
            // add(BooleanClause clause)与add(Query query, BooleanClause.Occur occur)
        }
        return query;
    }

    @Test
    public void testsearch() throws IOException, InvalidTokenOffsetsException {
        searchByKeywords("原告");
    }
}

