package cn.edu.nju.software.util;

import exception.NoEnumException;

/**
 * description:前后端变量名匹配工具,形式为：中文含义(lucene对应的域名，前端的key）
 * Created by gaoyw on 2018/4/14.
 */
public enum Search2Field {
    内容搜索("wsnr", "input"),
    标题搜索("bt", "title"),
    编号搜索("bh", "number");
    Search2Field() {
    }
    /**
     * 这个构造方法默认是私有的
     */
    Search2Field(String luceneField, String webField) {
        this.luceneField = luceneField;
        this.webField = webField;
    }

    private String luceneField;

    private String webField;

    public String getLuceneField() {
        return luceneField;
    }

    public void setLuceneField(String luceneField) {
        this.luceneField = luceneField;
    }

    public String getWebField() {
        return webField;
    }

    public void setWebField(String webField) {
        this.webField = webField;
    }

    public static String getLucenefieldFromWebfield(String webField) throws NoEnumException {
        Search2Field[] search2Fields = Search2Field.values();
        for (Search2Field search2Field:search2Fields){
            if (search2Field.getWebField() == webField){
                return search2Field.luceneField;
            }
        }
        throw new NoEnumException("Can't find the field of " + webField +", please add it in Search2Field.java");
    }

    public static void main(String[] args) {
        try {
            System.out.println(Search2Field.getLucenefieldFromWebfield("2222"));
        } catch (NoEnumException e) {
            e.printStackTrace();
        }
    }
}
