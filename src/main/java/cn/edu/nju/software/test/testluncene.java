package cn.edu.nju.software.test;

import cn.edu.nju.software.model.entity.SFBZHNRB;
import cn.edu.nju.software.service.ExampleService;
import cn.edu.nju.software.util.Constant;
import cn.edu.nju.software.util.StringUtil;
import cn.edu.nju.software.util.WordsSplit;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class testluncene {
    private static Logger log = Logger.getLogger(testluncene.class);
    private ApplicationContext ctx = null;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void wordSplit() {
        String keywords = "你好";
        WordsSplit.getWords(keywords);
    }

    @Test
    public void createIndexOfLucene() {
        ExampleService ExampleService = (ExampleService) ctx.getBean("exampleService");
        File indexDir = new File(Constant.IndexPath);
        if (!indexDir.exists()) {
            indexDir.mkdirs();
        }
        // 创建索引
        IndexWriterConfig iwc = new IndexWriterConfig(Constant.Analyzer);
        // 设置模式
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(FSDirectory.open(indexDir.toPath()),
                    iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc;
        List<SFBZHNRB> l = ExampleService.findAllSFBZHNR();
        for (SFBZHNRB sfbzhnrb : l) {
            doc = new Document();
            System.out.println(sfbzhnrb.getNR());
            Field wsnr ;
            if(StringUtil.isEmpty(sfbzhnrb.getNR())){
                wsnr = new TextField("wsnr", "", Field.Store.YES);
            } else {
                wsnr = new TextField("wsnr", sfbzhnrb.getNR(), Field.Store.YES);
            }
            Field bt = new StringField("bt", sfbzhnrb.getBT(), Field.Store.YES);
            Field bh = new IntField("bh", sfbzhnrb.getBH(), Field.Store.YES);
            doc.add(wsnr);
            doc.add(bh);
            doc.add(bt);
            try {
                // 将doc写回
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            doc = null;
        }
        try {
            //提交记录，添加到索引库里
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchIndex() throws IOException{
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs hits = null;
        ScoreDoc[] scoreDocs = null;
        Query query=null;
        try {
            QueryParser qp=new QueryParser("wsnr",Constant.Analyzer);//用于解析用户输入的工具
            query=qp.parse("原告");
        } catch (ParseException e) {
            // TODO: handle exception
        }
        TopDocs topDocs = searcher.search(query, 1000);
        int count = topDocs.totalHits;//根据关键词得到目录中中总的条目数
        System.out.println("共查询到条目数:"+count);
        scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc1 = scoreDocs[i].doc;
            log.info(scoreDocs[i]);
            Document document = searcher.doc(doc1);
            System.out.println("第" + i + "篇");
            System.out.println("编号:" + document.get("bh"));
            System.out.println(document.get("bt"));
            System.out.println(document.get("wsnr"));
        }
        reader.close();
    }

    @Test
    public void testboolsearch() throws IOException{
        //测试组合查询，这里是测试必须有原告和答辩
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        BooleanQuery query = new BooleanQuery();
        Term term1 = new Term("wsnr", "原告");
        Term term2 = new Term("wsnr", "答辩");
        TermQuery tQuery1 = new TermQuery(term1);
        TermQuery tQuery2 = new TermQuery(term2);
        BooleanClause clause=new BooleanClause(tQuery1, BooleanClause.Occur.MUST);
        BooleanClause clause2=new BooleanClause(tQuery2, BooleanClause.Occur.MUST);
        query.add(clause);
        query.add(clause2);
        TopDocs topDocs = searcher.search(query, 1000);
        int count = topDocs.totalHits;//根据关键词得到目录中中总的条目数
        System.out.println("共查询到条目数:"+count);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc1 = scoreDocs[i].doc;
            Document document = searcher.doc(doc1);
            System.out.println("第" + i + "篇");
            System.out.println("编号:" + document.get("bh"));
            System.out.println(document.get("bt"));
            System.out.println(document.get("wsnr"));
        }
        reader.close();
    }

    @Test
    public void testboolsearch2() throws IOException{
        //测试组合查询，这里是测试必须有原告和答辩,或者有法院和送达
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        BooleanQuery query = new BooleanQuery();
        BooleanQuery query1 = new BooleanQuery();
        BooleanQuery query2 = new BooleanQuery();
        Term term1 = new Term("wsnr", "原告");
        Term term2 = new Term("wsnr", "答辩");
        TermQuery tQuery1 = new TermQuery(term1);
        TermQuery tQuery2 = new TermQuery(term2);
        BooleanClause clause1=new BooleanClause(tQuery1, BooleanClause.Occur.MUST);
        BooleanClause clause2=new BooleanClause(tQuery2, BooleanClause.Occur.MUST);
        query1.add(clause1);
        query1.add(clause2);
        Term term3 = new Term("wsnr", "法院");
        Term term4 = new Term("wsnr", "送达");
        TermQuery tQuery3 = new TermQuery(term3);
        TermQuery tQuery4 = new TermQuery(term4);
        BooleanClause clause3=new BooleanClause(tQuery3, BooleanClause.Occur.MUST);
        BooleanClause clause4=new BooleanClause(tQuery4, BooleanClause.Occur.MUST);
        query2.add(clause3);
        query2.add(clause4);
        query.add(query1,BooleanClause.Occur.SHOULD);
        query.add(query2,BooleanClause.Occur.SHOULD);
        TopDocs topDocs = searcher.search(query, 1000);
        int count = topDocs.totalHits;//根据关键词得到目录中中总的条目数
        System.out.println("共查询到条目数:"+count);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc1 = scoreDocs[i].doc;
            Document document = searcher.doc(doc1);
            log.info("第" + i + "篇");
            log.info("编号:" + document.get("bh"));
            log.info(document.get("bt"));
            log.info(document.get("wsnr"));
        }
        reader.close();
    }

    @Test
    public void testgl() throws IOException, InvalidTokenOffsetsException {
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs hits = null;
        ScoreDoc[] scoreDocs = null;
        Query query=null;
        try {
            QueryParser qp=new QueryParser("wsnr",Constant.Analyzer);//用于解析用户输入的工具
            query=qp.parse("原告");
        } catch (ParseException e) {
            // TODO: handle exception
        }
        long startTime = System.currentTimeMillis(); //记录索引开始时间
        TopDocs topDocs = searcher.search(query, 1000);
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("匹配共耗时" + (endTime-startTime) + "毫秒");
        System.out.println("共查询到条目数:"+topDocs.totalHits);
        scoreDocs = topDocs.scoreDocs;

        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
        QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分
//        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer); //根据这个得分计算出一个片段
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

    // 同一个域里面可以存储多个值,但是查不到
    @Test
    public void testDivStore() throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriterConfig iwc = new IndexWriterConfig(Constant.Analyzer);
        // 设置模式
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter;
        indexWriter = new IndexWriter(directory, iwc);
        Document doc3 = new Document();
        doc3.add(new StringField("bh", "3", Field.Store.YES));
        doc3.add(new StringField("bh", "11", Field.Store.YES));
        doc3.add(new StringField("bh", "13", Field.Store.YES));
        doc3.add(new IntField("sortField", 3, Constant.Int_FIELD_TYPE_STORED_SORTED));
        indexWriter.addDocument(doc3);
        Document doc1 = new Document();
        doc1.add(new StringField("bh", "1", Field.Store.YES));
        doc1.add(new StringField("bh", "11", Field.Store.YES));
        doc1.add(new StringField("bh", "13", Field.Store.YES));
        doc1.add(new IntField("sortField", 1, Constant.Int_FIELD_TYPE_STORED_SORTED));
        indexWriter.addDocument(doc1);
        Document doc2 = new Document();
        doc2.add(new StringField("bh", "2", Field.Store.YES));
        doc2.add(new StringField("bh", "11", Field.Store.YES));
        doc2.add(new IntField("sortField", 2, Constant.Int_FIELD_TYPE_STORED_SORTED));
//        doc2.add(new StringField("bh", "13", Field.Store.YES));
        indexWriter.addDocument(doc2);
        indexWriter.commit();
        indexWriter.close();
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs hits = null;
        ScoreDoc[] scoreDocs = null;
        Term term=new Term("bh", "11");
        Query query=new TermQuery(term);
        SortField[] sortField = new SortField[1];
        sortField[0] = new SortField("sortField",SortField.Type.INT,false); // 对sortField的域进行降序升序排列
        Sort sort = new Sort(sortField);
        TopDocs topDocs = searcher.search(query, 1000, sort);
        int count = topDocs.totalHits;//根据关键词得到目录中中总的条目数
        System.out.println("共查询到条目数:"+count);
        scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc = scoreDocs[i].doc;
            log.info(scoreDocs[i]);
            Document document = searcher.doc(doc);
            log.info(document.getField("bh").stringValue());
            System.out.println("第" + i + "篇");
            System.out.println("编号:" + document.get("bh"));
        }
        reader.close();
    }
}

