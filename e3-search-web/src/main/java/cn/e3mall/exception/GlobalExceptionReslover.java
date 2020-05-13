package cn.e3mall.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志。。。。。");
		logger.info("系统发生异常！........");
		logger.error("系统发生异常！",ex);
		//发邮件,短信
		//显示错误页面
		ModelAndView andView=new ModelAndView();
		andView.setViewName("error/exception");
		return andView;
	}

}
