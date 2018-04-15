package cn.edu.nju.software.controller;

import cn.edu.nju.software.model.dto.Back2Search;
import cn.edu.nju.software.model.dto.SearchCondition;
import cn.edu.nju.software.service.SearchService;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class SearchController {
    private static final Logger log = Logger.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/content")
    @ResponseBody
    public List<Back2Search> searchContent(String input, String searchCondition, HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        String wsnr = "原告";
        log.info("input======" + input);
        log.info("searchCondition======" + searchCondition);
        JSONArray array = JSONArray.fromObject(searchCondition);
        List<SearchCondition> list = (List<SearchCondition>)JSONArray.toCollection(array, SearchCondition.class);
        log.info(list);
        List<Back2Search> back2SearchList = searchService.searchByContent(wsnr);
        return back2SearchList;
    }
    @RequestMapping(value = "/content2", method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        return "你好";
    }
}
