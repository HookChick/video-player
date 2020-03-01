package com.njau.playerservice.entity;

public class Account {

	private int aid;
	private int uid;
	private float balance;

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account [aid=" + aid + ", uid=" + uid + ", balance=" + balance
				+ "]";
	}

}
