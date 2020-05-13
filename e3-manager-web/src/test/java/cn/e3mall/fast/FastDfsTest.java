package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.untils.FastDFSClient;

public class FastDfsTest {
	@Test
	public void testUpload() throws Exception{
//		1、加载配置文件，配置文件中的内容就是tracker服务的地址。
//		配置文件内容：tracker_server=192.168.25.133:22122
		ClientGlobal.init("F:/Distributed_project/e3-manager-web/src/main/resources/conf/client.conf");
//		2、创建一个TrackerClient对象。直接new一个。
		TrackerClient client=new TrackerClient();
//		3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		TrackerServer trackerServer = client.getConnection();
//		4、创建一个StorageServer的引用，值为null
		StorageServer storageServer=null;
//		5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//		6、使用StorageClient对象上传图片。返回数组。包含组名和图片的路径D:/picture
		String[] upload_file = storageClient.upload_file("D:/picture/123.jpg","jpg", null);
		for (String string : upload_file) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testFastDas() throws Exception {
		FastDFSClient client=new FastDFSClient("F:/Distributed_project/e3-manager-web/src/main/resources/conf/client.conf");
		String file = client.uploadFile("D:/picture/1112.jpg");
		System.out.println(file);
	}
}
