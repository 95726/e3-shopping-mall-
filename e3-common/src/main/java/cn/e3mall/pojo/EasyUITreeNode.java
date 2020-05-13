package cn.e3mall.pojo;

import java.io.Serializable;

//新增商品的树结构
public class EasyUITreeNode implements Serializable{
	private long id;
	private String state;
	private String text;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
