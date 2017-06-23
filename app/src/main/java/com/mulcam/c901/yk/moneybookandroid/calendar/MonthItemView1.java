package com.mulcam.c901.yk.moneybookandroid.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mulcam.c901.yk.moneybookandroid.R;

/**
 * 일자에 표시하는 텍스트뷰 정의
 * 
 * @author Mike
 */

/**
 * Created by user on 2016-08-10.
 */
public class MonthItemView1 extends LinearLayout {
	private TextView dayTv;
	private TextView dayIncomeTv;
	private TextView dayExpenseTv;
	private MonthItem item;

	public MonthItemView1(Context context) {
		super(context);
		init(context);
	}

	public MonthItemView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.month_item, this, true);

		dayTv = (TextView) findViewById(R.id.month_item_day);
		dayExpenseTv = (TextView) findViewById(R.id.month_item_expense);
		dayIncomeTv = (TextView) findViewById(R.id.month_item_income);

		setBackgroundColor(Color.WHITE);
	}
	

	public MonthItem getItem() {
		return item;
	}

	public void setItem(MonthItem item) {
		this.item = item;
		
		int day = item.getDay();
		if (day != 0) {
			dayTv.setText(String.valueOf(day));
		} else {
			dayTv.setText("");
		}

		int income = item.getIncome();
		if (income != 0) {
			dayIncomeTv.setText(String.valueOf(income));
		} else {
			dayIncomeTv.setText("");
		}

		int expense = item.getExpense();
		if (expense != 0) {
			dayExpenseTv.setText(String.valueOf(expense));
		} else {
			dayExpenseTv.setText("");
		}

	}

	public TextView getDayTv() {
		return dayTv;
	}

	public void setDayTv(TextView dayTv) {
		this.dayTv = dayTv;
	}

	public TextView getDayIncomeTv() {
		return dayIncomeTv;
	}

	public void setDayIncomeTv(TextView dayIncomeTv) {
		this.dayIncomeTv = dayIncomeTv;
	}

	public TextView getDayExpenseTv() {
		return dayExpenseTv;
	}

	public void setDayExpenseTv(TextView dayExpenseTv) {
		this.dayExpenseTv = dayExpenseTv;
	}
}
