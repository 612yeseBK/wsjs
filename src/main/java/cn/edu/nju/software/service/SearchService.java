package cn.edu.nju.software.service;

import cn.edu.nju.software.model.dto.Back2Search;
import cn.edu.nju.software.model.dto.SearchCondition;
import cn.edu.nju.software.util.Constant;
import cn.edu.nju.software.util.Search2Field;
import cn.edu.nju.software.util.WordsSplit;
import exception.NoEnumException;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * description:搜索业务
 * Created by gaoyw on 2018/4/7.
 */
@Service("searchService")
public class SearchService {
    private static Logger log = Logger.getLogger(SearchService.class);

    /**
     * 根据搜索框的输入和条件的选择去查询
     * @param content 搜索框的输入
     * @param searchConditionList 额外条件
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     * @throws NoEnumException
     */
    public Map searchByContentAndCondition(String content, List<SearchCondition> searchConditionList,int curpage, int pagesize) throws IOException, InvalidTokenOffsetsException, NoEnumException {
        BooleanQuery generalQuery = new BooleanQuery();
        BooleanQuery contentQuery = getQueryByContent(content);
        generalQuery.add(contentQuery,BooleanClause.Occur.MUST);
        if (searchConditionList.size() != 0){
            BooleanQuery conditionQuery = getQueryFromConditions(searchConditionList);
            generalQuery.add(conditionQuery,BooleanClause.Occur.MUST);
        }
        log.info("本次查询的内容为：" + content);
        log.info("多条件查询为：" + searchConditionList);
        return searchByQuery(generalQuery,curpage,pagesize);
    }

    /**
     * 利用查询语句去搜索相应的数据
     * 这里面一堆页数的目的是为了分页的选项卡最大值只比当前页多5页，如果不足五页则显示实际页数
     * @param booleanQuery 查询语句
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    public Map searchByQuery(BooleanQuery booleanQuery, int curpage,int pagesize) throws IOException, InvalidTokenOffsetsException {
        Map map = new HashMap();
        int thisPageBegin = (curpage-1)*pagesize;//表示搜索到这一页开头的文书数目
        int thisPageEnd = curpage*pagesize;//表示搜索到这一页末尾的文书数目
        int endNum = (curpage+5)*pagesize; // 表示往后给它多个五页的页码选项卡，不给多
        File indexDir = new File(Constant.IndexPath);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        long startTime = System.currentTimeMillis();
        TopDocs topDocs = searcher.search(booleanQuery, endNum);
        long endTime = System.currentTimeMillis();
        log.info("匹配共耗时" + (endTime - startTime) + "毫秒");
        log.info("共查询到条目数:" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        int backNum = scoreDocs.length;//返回给前端的总条目数
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>"); //如果不指定参数的话，默认是加粗，即<b><b/>
        QueryScorer scorer = new QueryScorer(booleanQuery);
        Fragmenter fragmenter = new SimpleFragmenter(Constant.LengthOfBack);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Back2Search> back2SearchList = new ArrayList<>();
        for (int i = thisPageBegin; i < ((thisPageEnd<backNum)?thisPageEnd:backNum); i++) {
            int doc1 = scoreDocs[i].doc;
            Document document = searcher.doc(doc1);
            Back2Search back2Search = new Back2Search(document);
            back2Search.setDocid(doc1);
            String content = document.get(Search2Field.文书内容.getLuceneField());
            TokenStream tokenStream = Constant.Analyzer.tokenStream(Search2Field.文书内容.getLuceneField(), new StringReader(content));
            String hlContent = highlighter.getBestFragment(tokenStream, content);
            back2Search.setHLContent(hlContent);
            back2SearchList.add(back2Search);
        }
        reader.close();
        map.put("list",back2SearchList);
        map.put("num", backNum);
        return map;
    }

    /**
     * 搜索内容以空格分开，对句子数组各自分词，每个句子里面的词是MUST关系，句子与句子之间是SHOULD关系
     * @param content 搜索内容将会以空格分开
     * @return
     */
    public BooleanQuery getQueryByContent(String content){
        String contentField = Search2Field.文书内容.getLuceneField();
        BooleanQuery booleanQuery = new BooleanQuery();
        String[] sentences = content.trim().split("\\s+");
        List<String> sentenceList = Arrays.asList(sentences);
        for (String sentence:sentenceList){
            List<String> words = WordsSplit.getWords(sentence);
            booleanQuery.add(getQueryFormWords(contentField,words,BooleanClause.Occur.MUST),BooleanClause.Occur.SHOULD);
        }
        return booleanQuery;
    }

    /**
     * 将前端返回的搜索条件合成一个BooleanQuery
     * @param searchConditionList 前端返回的搜索条件
     * @return
     * @throws NoEnumException
     */
    public BooleanQuery getQueryFromConditions(List<SearchCondition> searchConditionList) throws NoEnumException {
        BooleanQuery bcBq = new BooleanQuery();
        for (SearchCondition searchCondition: searchConditionList){
            String luceneField = Search2Field .getLucenefieldFromWebfield(searchCondition.getId());
            if (searchCondition.getId() == Search2Field.发布时间.getWebField()){
                Query query = NumericRangeQuery.newIntRange(luceneField, Integer.valueOf(searchCondition.getValue().get(0)), Integer.valueOf(searchCondition.getValue().get(1)), true, true);
                BooleanClause clause = new BooleanClause(query, BooleanClause.Occur.MUST);
                bcBq.add(clause);
            } else{
                BooleanQuery booleanQuery = getQueryFormWords(luceneField, searchCondition.getValue(),BooleanClause.Occur.SHOULD);
                bcBq.add(booleanQuery,BooleanClause.Occur.MUST);
            }
        }
        return bcBq;
    }

    /**
     * 用于生成最基本的BooleanQuery
     * @param field 要查询的lucene的域名
     * @param keywords 需要查询的关键字的collection
     * @param occur 这些关键字之间的关系
     * @return
     */
    public BooleanQuery getQueryFormWords(String field, List<String> keywords,BooleanClause.Occur occur) {
        BooleanQuery query = new BooleanQuery();
        for (String keyword : keywords) {
            Term term = new Term(field, keyword);
            TermQuery tQuery = new TermQuery(term);
            BooleanClause clause = new BooleanClause(tQuery, occur);
            query.add(clause);
        }
        return query;
    }

    /**
     * 通过dicid获取document某个域的内容
     * @param fieldName
     * @param docid
     * @return
     * @throws IOException
     */
    public String getFieldContentFromDocId(String fieldName,int docid) throws IOException {
        File indexDir = new File(Constant.IndexPath);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        Document document = searcher.doc(docid);
        return document.get(fieldName);
    }

    @Test
    public void testDivContent(){
        String content = " nihao 不会  对     有趣   好的  ";
        content = content.trim();
        String[] tt=content.split("\\s+");
        for (String t:tt){
            System.out.println("==" + t + "==");
        }
        System.out.println("######");
    }
}

