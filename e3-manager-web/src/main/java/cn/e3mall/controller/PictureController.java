package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import cn.e3mall.untils.FastDFSClient;
import cn.e3mall.untils.JsonUtils;

@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {
			//图片上传服务器
			FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
			//取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			//得到图片和文件扩展名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
			//补充完整的url
			url=IMAGE_SERVER_URL+url;
			//返回map
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			JsonUtils.objectToJson(result);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			//5、返回map
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}	
	}
}
