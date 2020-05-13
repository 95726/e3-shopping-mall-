package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 *  主页跳转Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class PageController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/")
	public String getPage() {
		return "index";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {	
		return page;	
	}
	//商品分页实现
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getIteList(Integer page,Integer rows) {
		//调用service服务查询List
		EasyUIDataGridResult itemList = itemService.getItemList(page, rows);
		return itemList;
		
	}
	@RequestMapping("/rest/page/item-edit")
	public String showEdit() {	
		return "item-edit";
		
	}
}
