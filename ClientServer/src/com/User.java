package com;

import java.io.Serializable;

public class User implements Serializable {
	public User(String name, String password) {
		this.name = name;
		this.password = password;
		this.numbers = new int[3];
		numbers[0] = -1;
		numbers[1] = -1;
		numbers[2] = -1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setNumbers(int[] tab) {
		this.numbers = tab;
	}
	public int[] getNumbers() {
		return numbers;
	}
	private String name;
	private String password;
	private int[] numbers;
}
