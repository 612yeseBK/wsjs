package cn.edu.nju.software.model.dto;

import java.util.List;

/**
 * description:用于接受前端传来的除了内容搜索外的额外选项卡
 * Created by gaoyw on 2018/4/15.
 */
public class SearchCondition {
    private String id;
    private List<String> value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String val = "";
        for (String s:value) {
            val = val + " " + s;
        }
        return "id:" + id + ";value:" + val;
    }
}
