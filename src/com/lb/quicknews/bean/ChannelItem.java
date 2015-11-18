package com.lb.quicknews.bean;

import java.io.Serializable;

public class ChannelItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8711399755628525420L;
	private Integer id;
	private String name;
	private Integer orderId;
	private Integer selected;

	public ChannelItem() {

	}

	public ChannelItem(Integer id, String name, Integer orderId,
			Integer selected) {
		super();
		this.id = id;
		this.name = name;
		this.orderId = orderId;
		this.selected = selected;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "ChannelItem [id=" + id + ", name=" + name + ", orderId="
				+ orderId + ", selected=" + selected + "]";
	}

}
