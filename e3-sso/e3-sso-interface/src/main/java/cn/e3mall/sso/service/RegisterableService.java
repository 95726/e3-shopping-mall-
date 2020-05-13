package cn.e3mall.sso.service;

import cn.e3mall.pojo.TbUser;
import cn.e3mall.untils.E3Result;

public interface RegisterableService {
	 E3Result checkData(String param,int type);
	 E3Result register(TbUser tbUser);
}
