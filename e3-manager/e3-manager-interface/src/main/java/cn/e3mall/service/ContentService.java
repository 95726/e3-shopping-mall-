package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.untils.E3Result;

public interface ContentService {
	public E3Result addContent(TbContent tbContent);
	public List<TbContent>getContentListByCid(long cid);
}
