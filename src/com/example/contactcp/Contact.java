package com.example.contactcp;

import java.io.Serializable;

import android.net.Uri;

/**
 * ��ϵ��ʵ��
 */
public class Contact implements Serializable {
	private String name; // ����
	private String phone; // ����
	private Uri headIconUri; // ͷ��Uri��ַ

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
