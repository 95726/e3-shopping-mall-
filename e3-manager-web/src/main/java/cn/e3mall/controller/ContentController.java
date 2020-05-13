package cn.e3mall.controller;
/*
 * 内容管理Controlled
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.service.ContentService;
import cn.e3mall.untils.E3Result;

@Controller
public class ContentController {
	//添加
	@Autowired
	private ContentService contentService;
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent tbcontent) {
		E3Result result = contentService.addContent(tbcontent);
		return result;
		
	}
	
}
