package cn.e3mall.serach.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {
	public SearchResult search(String keyword,int page,int rows) throws Exception;
}
