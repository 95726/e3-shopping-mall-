package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import cn.e3mall.untils.E3Result;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	//根据Id查询用户
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		E3Result result = itemService.addItem(item, desc);
		return result;
	}

	//商品的批量删除
		@RequestMapping("/rest/item/delete")
		@ResponseBody
		public E3Result deleteList(Long ids[]) {
			E3Result result = itemService.deleteList(ids);
			return result;
	
		}
	//商品下架/rest/item/instock
		@RequestMapping("/rest/item/instock")
		@ResponseBody
		public E3Result updateInstock(Long ids[]) {
			E3Result result = itemService.updateInstock(ids);
			return result;
	
		}
		
	//商品上/rest/item/reshelf
		@RequestMapping("/rest/item/reshelf")
		@ResponseBody
		public E3Result updateReshelf(Long ids[]) {
			E3Result result = itemService.updateReshelf(ids);
			return result;
	
		}
		
	//商品描述数据回显
		@RequestMapping("/rest/item/query/item/desc/{id}")
		@ResponseBody
		public TbItemDesc echoItemDesc(@PathVariable Long id) {
			System.out.println(id);
			TbItemDesc desc = itemService.echoItemDesc(id);
			return desc;	
		}
		
}
