package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parent) {
		//根据ID查找子节点列表
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parent);
		//执行查询效果
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//创建返回值结果
		List<EasyUITreeNode> resultList=new ArrayList<EasyUITreeNode>();
		//把列表换成ListEasyUITreeNode
		for (TbItemCat ItemCat : list) {
			EasyUITreeNode node=new EasyUITreeNode();
			//设置属性
			node.setId(ItemCat.getId());
			node.setText(ItemCat.getName());
			node.setState(ItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
		
		return resultList;
	}

}
