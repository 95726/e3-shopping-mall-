package cn.e3mall.service;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.untils.E3Result;

public interface ItemService {
	public TbItem getItemById(long itemId);
	public EasyUIDataGridResult getItemList(int page,int rows);
	public E3Result addItem(TbItem item,String desc);
	public TbItemDesc getItemDesc(long itemId);
	
	public E3Result deleteList(Long ids[]);
	public E3Result updateInstock(Long ids[]);
	public E3Result updateReshelf(Long ids[]);
	public TbItemDesc echoItemDesc(Long id);
}