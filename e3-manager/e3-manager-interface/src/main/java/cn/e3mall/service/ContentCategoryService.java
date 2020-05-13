package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.untils.E3Result;


public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(Long parentId)throws Exception;
	E3Result addConetntCatfory(long parntId,String name);
}
