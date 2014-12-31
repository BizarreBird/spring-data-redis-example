package org.nivance.redisexam.bean;

import java.util.Date;

import lombok.Data;

@Data
public class UserInfo {
	private String name;
	private int age;
	private String id;
	private Date birthday;
}
