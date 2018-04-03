package cn.edu.nju.software.controller;

import cn.edu.nju.software.model.dto.Back2Search;
import cn.edu.nju.software.service.SearchService;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public List<Back2Search> searchContent(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidTokenOffsetsException {
        String wsnr = request.getParameter("wsnr");
        List<Back2Search> back2SearchList = searchService.searchByKeywords(wsnr);
        return back2SearchList;
    }
}
