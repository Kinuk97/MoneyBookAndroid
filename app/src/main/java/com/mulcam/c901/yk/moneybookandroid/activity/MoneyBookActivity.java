package com.mulcam.c901.yk.moneybookandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mulcam.c901.yk.moneybookandroid.R;
import com.mulcam.c901.yk.moneybookandroid.calendar.MonthAdapter;
import com.mulcam.c901.yk.moneybookandroid.calendar.MonthItem;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 그리드뷰를 이용해 월별 캘린더를 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MoneyBookActivity extends AppCompatActivity {
    private MoneybookService moneybookService;
    private GridView monthView;
    private MonthAdapter monthViewAdapter;

    /**
     * 월을 표시하는 텍스트뷰
     */
    private TextView monthText;

    /**
     * 현재 연도
     */
    private int curYear;

    /**
     * 현재 월
     */
    private int curMonth;
    private TextView incomeTv;
    private TextView expenseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moneybook);

        // 월별 캘린더 뷰 객체 참조
        monthView = (GridView) findViewById(R.id.mb_monthView);
        incomeTv = (TextView) findViewById(R.id.mb_income_tv);
        expenseTv = (TextView) findViewById(R.id.mb_expense_tv);
        monthViewAdapter = new MonthAdapter(this);
        monthView.setAdapter(monthViewAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080/MoneyBookProject/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        moneybookService = retrofit.create(MoneybookService.class);

        Call<HashMap<String, Object>> call = moneybookService.moneybookList(1);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                HashMap<String, Object> moneybookList = response.body();
                incomeTv.setText(moneybookList.get("monthIncome").toString());
                expenseTv.setText(moneybookList.get("monthExpense").toString());

            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                Toast.makeText(MoneyBookActivity.this, "리스트를 받아오는 데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // 리스너 설정
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 현재 선택한 일자 정보 표시
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();

                Log.d("MainActivity", "Selected : " + day);
            }
        });


        monthText = (TextView) findViewById(R.id.mb_month_text);
        setMonthText();

        // 이전 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.mb_month_previous_btn);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.mb_month_next_btn);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

    }

    /**
     * 월 표시 텍스트 설정
     */
    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }

}
