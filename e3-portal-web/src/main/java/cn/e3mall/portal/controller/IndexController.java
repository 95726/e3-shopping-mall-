package cn.e3mall.portal.controller;
/*
 * 前台页面的展示
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.service.ContentService;

@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	@Value("${CONTENT_LUNBO_ID}")
	private Long CONTENT_LUNBO_ID; 
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		//查询内容列表
		List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
}
