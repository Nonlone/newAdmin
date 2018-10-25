package com.feitai.admin.core.vo;

import lombok.Data;

/**
 * 列表项。
 * 
 * 比如：Select、List
 * @author chenxi
 *
 */
@Data
public class ListItem {

	private String text;

	private String value;


	public ListItem(String text, String value) {
		super();
		this.text = text;
		this.value = value;
	}

}
