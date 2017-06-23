package com.mulcam.c901.yk.moneybookandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mulcam.c901.yk.moneybookandroid.R;
import com.mulcam.c901.yk.moneybookandroid.calendar.MoneybookAdapter;
import com.mulcam.c901.yk.moneybookandroid.calendar.MonthAdapter;
import com.mulcam.c901.yk.moneybookandroid.calendar.MonthItem;
import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookCalculator;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookService;
import com.mulcam.c901.yk.moneybookandroid.setting.MoneybookDBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private MoneybookDBManager dbManager;
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
    private int id_index;
    private TextView incomeTv;
    private TextView expenseTv;
    private monthlyAsync ma;
    private ListView dayLv;
    SharedPreferences mbPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moneybook);
        dbManager = new MoneybookDBManager(this);
        mbPrefs = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        id_index = mbPrefs.getInt("id_index", 0);

        // 월별 캘린더 뷰 객체 참조
        monthView = (GridView) findViewById(R.id.mb_monthView);
        incomeTv = (TextView) findViewById(R.id.mb_income_tv);
        expenseTv = (TextView) findViewById(R.id.mb_expense_tv);
        dayLv = (ListView) findViewById(R.id.mb_day_list);

        Date date = new Date();
        monthViewAdapter = new MonthAdapter(this, dbManager, id_index);
        monthView.setAdapter(monthViewAdapter);

        List<MoneyBook> list = dbManager.selectMoneyBookList(id_index);
        ma = new monthlyAsync(list, incomeTv, expenseTv, dbManager);
        ma.execute();

        // 리스너 설정
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 현재 선택한 일자 정보 표시
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();

                Log.d("MainActivity", "Selected : " + day);
                ClickDayAsync dayAsync = new ClickDayAsync(dayLv, day, id_index, dbManager);
                dayAsync.execute();
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

                List<MoneyBook> list = dbManager.selectMoneyBookList(id_index);
                ma = new monthlyAsync(list, incomeTv, expenseTv, dbManager);
                ma.execute();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.mb_month_next_btn);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();

                List<MoneyBook> list = dbManager.selectMoneyBookList(id_index);
                ma = new monthlyAsync(list, incomeTv, expenseTv, dbManager);
                ma.execute();
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

    //    한달 수입, 하루치 계산해서 넣기
    class monthlyAsync extends AsyncTask<Void, Void, HashMap<String, Object>> {
        private MoneybookDBManager dbManager;
        private List<MoneyBook> mbList;
        private TextView incomeTv;
        private TextView expenseTv;
        private ProgressDialog dialog;

        public monthlyAsync(List<MoneyBook> mbList, TextView incomeTv, TextView expenseTv, MoneybookDBManager dbManager) {
            this.mbList = mbList;
            this.incomeTv = incomeTv;
            this.expenseTv = expenseTv;
            this.dbManager = dbManager;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MoneyBookActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setTitle("월별 보기");
            dialog.setMessage("데이터를 가져오는 중입니다.");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            dialog.setProgressStyle(1);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> aVoid) {
            super.onPostExecute(aVoid);
            incomeTv.setText(aVoid.get("income").toString());
            expenseTv.setText(aVoid.get("expense").toString());
            dialog.dismiss();
        }

        @Override
        protected HashMap<String, Object> doInBackground(Void... params) {
            HashMap<String, Object> monthlyMb = new HashMap<>();
            int income = 0;
            int expense = 0;
            for (MoneyBook mb : mbList) {
                publishProgress();
                Date date = mb.getM_date();
                if (date.getMonth() == curMonth) {
                    if (mb.getCategory().equals("income"))
                        income += mb.getPrice();
                    else
                        expense += mb.getPrice();

                }
            }
            monthlyMb.put("income", income);
            monthlyMb.put("expense", expense);

            return monthlyMb;
        }
    }

    class ClickDayAsync extends AsyncTask<Void, Void, List<MoneyBook>> {
        private int selected;
        private int id_index;
        private MoneybookDBManager dbManager;
        private ListView lv;
        private MoneybookAdapter adapter;

        public ClickDayAsync(ListView lv, int selected, int id_index, MoneybookDBManager dbManager) {
            this.lv = lv;
            this.selected = selected;
            this.id_index = id_index;
            this.dbManager = dbManager;
        }

        @Override
        protected void onPostExecute(List<MoneyBook> aVoid) {
            super.onPostExecute(aVoid);
            adapter = new MoneybookAdapter(getApplicationContext(), R.layout.day_list, aVoid);
            lv.setAdapter(adapter);
        }

        @Override
        protected List<MoneyBook> doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(curYear, curMonth, selected);
            Date date = calendar.getTime();
            List<MoneyBook> dayList = dbManager.selectDayList(id_index, date);
            Log.d("dayList~~~", dayList.toString());

            return dayList;
        }
    }

}
