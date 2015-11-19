package com.lb.quicknews.bean;

import java.util.List;

public class ImagesModle extends BaseModle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 584711525660011726L;
	private String docid;
	private String title;
	private List<String> imgList;

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	@Override
	public String toString() {
		return "ImagesModle [docid=" + docid + ", title=" + title
				+ ", imgList=" + imgList + "]";
	}

}
