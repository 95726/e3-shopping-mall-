package cn.e3mall.mapper;

import java.util.List;
import cn.e3mall.common.pojo.SearchItem;


public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
