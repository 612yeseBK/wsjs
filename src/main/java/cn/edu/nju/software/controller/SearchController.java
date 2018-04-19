package cn.edu.nju.software.controller;

import cn.edu.nju.software.model.dto.SearchCondition;
import cn.edu.nju.software.service.SearchService;
import cn.edu.nju.software.service.WszhService;
import exception.NoEnumException;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private static final Logger log = Logger.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;
    @Autowired
    WszhService wszhService;

    /**
     * 搜索 todo 还需要增加分页的页码数目
     * @param input
     * @param searchCondition
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/content")
    @ResponseBody
    public Map searchContent(String input, String searchCondition, int curpage, HttpServletRequest request, HttpServletResponse response) {
        int pagesize = 10;
        JSONArray array = JSONArray.fromObject(searchCondition);
        List<SearchCondition> searchConditionList = (List<SearchCondition>)JSONArray.toCollection(array, SearchCondition.class);
        Map map = new HashMap();
        try {
            map = searchService.searchByContentAndCondition(input,searchConditionList,curpage,pagesize);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } catch (NoEnumException e) {
            e.printStackTrace();
        }
        map.put("pagesize",pagesize);
        return map;
    }

    /**
     * 获取条件列表里面的选项
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    @RequestMapping(value = "/getLists")
    @ResponseBody
    public Map getLists(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        Map map = new HashMap();
        String[] courts = {"天津一院","天津二院","天津三院"};
        String[] types = {"民事","行政","刑事"};
        map.put("courts", courts);
        map.put("types", types);
        return map;
    }

    /**
     * 获取文书的详情
     * @param wjbh 文书的id号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getDetail")
    @ResponseBody
    public List<Map> getDetail(String wjbh, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        List<Map> list = new ArrayList<>();
        list = wszhService.findContentByWjbh(Integer.parseInt(wjbh));
        return list;
    }

    @RequestMapping(value = "/content2", method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        return "你好";
    }

}
