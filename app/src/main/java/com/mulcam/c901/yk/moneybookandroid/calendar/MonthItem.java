package com.mulcam.c901.yk.moneybookandroid.calendar;

/**
 * 일자 정보를 담기 위한 클래스 정의
 * 
 * @author Mike
 *
 */
public class MonthItem {

	private int dayValue;
	private int income;
	private int expense;

	public MonthItem() {
		
	}
	
	public MonthItem(int day) {
		dayValue = day;
	}

	public MonthItem(int day, int income, int expense) {
		dayValue = day;
		this.income = income;
		this.expense = expense;
	}
	
	public int getDay() {
		return dayValue;
	}

	public void setDay(int day) {
		this.dayValue = day;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getExpense() {
		return expense;
	}

	public void setExpense(int expense) {
		this.expense = expense;
	}
}
