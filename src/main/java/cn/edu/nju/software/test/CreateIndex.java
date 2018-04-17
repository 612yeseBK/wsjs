package cn.edu.nju.software.test;

import cn.edu.nju.software.model.dao.SFBZHNRBDao;
import cn.edu.nju.software.model.dao.SFBZHWJBDao;
import cn.edu.nju.software.service.WszhService;
import cn.edu.nju.software.model.dto.SFBZHModel;
import cn.edu.nju.software.util.StringUtil;

import cn.edu.nju.software.util.Constant;

import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.store.SimpleFSDirectory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateIndex{
    private static ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    static SFBZHWJBDao sfbzhwjbDao = (SFBZHWJBDao) appContext.getBean("SFBZHWJBDao");
    static SFBZHNRBDao sfbzhnrbDao = (SFBZHNRBDao)appContext.getBean("SFBZHNRBDao");
    WszhService WszhService = (WszhService) appContext.getBean("wszhService");


    /**
     * 创建索引
     * 如果文件夹不存在，则需要首次创建索引，否则只需增量索引
     */
    @Test
    public void buildIndex(){
        try{
            File file = new File(Constant.IndexPath);
            if(!((file.exists()) && (file.listFiles().length > 1))){
                this.firstIndex();
            }else{
                this.updateIndex();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化索引库indexWriter
     * 一旦indexWriter创建完成，再改变IndexWriterConfig的配置，对indexWriter将不产生影响
     * @param OpenMOdeType
     * @param fileURL
     * @return
     */
    private IndexWriter initLucene(IndexWriterConfig.OpenMode OpenMOdeType, String fileURL){
        try{
            //创建索引
            IndexWriterConfig iwc = new IndexWriterConfig(Constant.Analyzer);
            // 设置模式
            iwc.setOpenMode(OpenMOdeType);
            //创建目录
            Directory fileDir = new SimpleFSDirectory((new File(fileURL)).toPath());
            //创建索引库
            IndexWriter indexWriter = new IndexWriter(fileDir,iwc);
            return indexWriter;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 第一次创建索引
     */
    private void firstIndex(){
        IndexWriter indexWriter = null;
        try{
            //获取数据
            List<SFBZHModel> results = WszhService.findWS();

            //若数据为空或者不存在，则返回；否则添加索引
            if(results.size() == 0 || null == results){
                return;
            }else{
                //获取索引库
                indexWriter = this.initLucene(IndexWriterConfig.OpenMode.CREATE_OR_APPEND,Constant.IndexPath);
                //添加Fields
                this.addFields(results, indexWriter);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //若第一次打开索引文件需要commit否则会报no segment
            try{
                if(null != indexWriter){
                    indexWriter.commit();
                    indexWriter.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * 更新索引（lucene本身不支持更新）
     * 通过删除索引然后再建立索引来更新
     */
    private void updateIndex(){
        IndexReader indexReader = null;
        IndexWriter indexWriterAdd = null;
        try{
            //获取数据
            List<SFBZHModel> results = WszhService.findWS();

            File fileAdd = new File(Constant.AddIndexPath);
            //如果文件夹不存在，创建
            if(!fileAdd.exists()){
                fileAdd.mkdir();
            }
            //创建IndexReader
            File file = new File(Constant.IndexPath);
            Directory dir = FSDirectory.open(file.toPath());
            indexReader = DirectoryReader.open(dir);

            long startTime = System.currentTimeMillis();

            //检索最新添加的数据是否索引
            List<SFBZHModel> updateDatas = new ArrayList<>();
            for(SFBZHModel updateData : results){
                //是否在索引库 标志符
                boolean flag = this.isInIndex(String.valueOf(updateData.getWJBH()),indexReader);
                if(flag){
                    //将不在索引库的数据，添加到updateDatas中
                    updateDatas.add(updateData);
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("剔除数据耗时：" + (endTime - startTime) + "ms");

            //添加索引
            if((updateDatas.size() == 0) || (null == updateDatas)){
                return;
            }else{
                if (!((file.exists()) && (file.listFiles().length > 3))){
                    return;
                }else if(!((fileAdd.exists()) && (fileAdd.listFiles().length > 3))){
                    indexWriterAdd = this.initLucene(IndexWriterConfig.OpenMode.CREATE_OR_APPEND,Constant.AddIndexPath);
                    this.addFields(updateDatas, indexWriterAdd);
                }else{
                    indexWriterAdd = this.initLucene(IndexWriterConfig.OpenMode.CREATE, Constant.AddIndexPath);
                    this.addFields(updateDatas, indexWriterAdd);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(null != indexWriterAdd){
                    indexWriterAdd.commit();
                }
                if (null != indexReader){
                    indexReader.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }


    /**
     * 添加Fields
     * @param results
     * @param indexWriter
     */
    private void addFields(List<SFBZHModel> results, IndexWriter indexWriter){
        try{
            long startTime = System.currentTimeMillis();
            for(SFBZHModel sfbzhModel : results){
                //创建Document
                Document doc = new Document();
                //创建Field
                doc.add(new TextField(Constant.Index_Wsnr, sfbzhModel.getNR()+"", Field.Store.YES));
                doc.add(new StringField(Constant.Index_Zcr,sfbzhModel.getZCR()+"",Field.Store.YES));
                doc.add(new StringField(Constant.Index_Llr,sfbzhModel.getLLR()+"",Field.Store.YES));
                doc.add(new IntField(Constant.Index_Wjbh,sfbzhModel.getWJBH(),Field.Store.YES));
                doc.add(new StringField(Constant.Index_Bzhwjmc,sfbzhModel.getBZHWJMC()+"",Field.Store.YES));
                doc.add(new StringField(Constant.Index_Cbdw,sfbzhModel.getCBDW()+"",Field.Store.YES));
                doc.add(new StringField(Constant.Index_Xbdw,sfbzhModel.getXBDW()+"",Field.Store.YES));
                //将索引添加到实时中去
                indexWriter.addDocument(doc);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("创建索引耗时：" + (endTime - startTime) + "ms");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //若第一次打开索引文件需要commit否则会报no segment
            try{
                if(null != indexWriter){
                    indexWriter.commit();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * 检查数据是否在索引库中
     * @param id
     * @param p_indexReader
     * @return
     */
    private boolean isInIndex(String id, IndexReader p_indexReader){
        boolean flag = true;
        try{
            for(int i = 0; i < p_indexReader.numDocs(); i++){
                Document doc = p_indexReader.document(i);
                if(id.equals(doc.getField(Constant.Index_Wjbh))){
                    flag = false;
                    return flag;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }


    @Test
    public void searchIndex() throws IOException {
        File indexDir = new File(Constant.IndexPath); //获取索引
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs hits = null;
        ScoreDoc[] scoreDocs = null;
        Query query=null;
        try {
            QueryParser qp=new QueryParser("zcr",Constant.Analyzer);//用于解析用户输入的工具
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
            Document document = searcher.doc(doc1);
            System.out.println("第" + (i+1) + "篇");
            System.out.println("编号:" + document.get("wjbh"));
            System.out.println(document.get("wsnr"));
        }
        reader.close();
    }



}