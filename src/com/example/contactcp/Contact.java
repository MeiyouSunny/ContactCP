package com.example.contactcp;

import java.io.Serializable;

import android.net.Uri;

/**
 * 联系人实体
 */
public class Contact implements Serializable {
	private String name; // 姓名
	private String phone; // 号码
	private Uri headIconUri; // 头像Uri地址

	public Contact() {
	}

	public Contact(String name, String phone, Uri headIconUri) {
		this.name = name;
		this.phone = phone;
		this.headIconUri = headIconUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Uri getHeadIconUri() {
		return headIconUri;
	}

	public void setHeadIconUri(Uri headIconUri) {
		this.headIconUri = headIconUri;
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", phone=" + phone + ", headIconUri="
				+ headIconUri + "]";
	}

}
