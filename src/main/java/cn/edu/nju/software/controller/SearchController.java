package cn.edu.nju.software.controller;

import cn.edu.nju.software.model.dto.Back2Search;
import cn.edu.nju.software.model.dto.SearchCondition;
import cn.edu.nju.software.service.SearchService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private static final Logger log = Logger.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    /**
     * 搜索
     * @param input
     * @param searchCondition
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/content")
    @ResponseBody
    public List<Back2Search> searchContent(String input, String searchCondition, HttpServletRequest request, HttpServletResponse response) {
        JSONArray array = JSONArray.fromObject(searchCondition);
        List<SearchCondition> searchConditionList = (List<SearchCondition>)JSONArray.toCollection(array, SearchCondition.class);
        List<Back2Search> back2SearchList = null;
        try {
            back2SearchList = searchService.searchByContentAndCondition(input,searchConditionList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } catch (NoEnumException e) {
            e.printStackTrace();
        }
        return back2SearchList;
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

    @RequestMapping(value = "/content2", method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        return "你好";
    }
}
