package com.mulcam.c901.yk.moneybookandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mulcam.c901.yk.moneybookandroid.R;
import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;
import com.mulcam.c901.yk.moneybookandroid.model.MoneyBookTemp;
import com.mulcam.c901.yk.moneybookandroid.service.LoginService;
import com.mulcam.c901.yk.moneybookandroid.setting.MoneybookDBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by student on 2017-06-14.
 */

public class LoginActivity extends Activity {
    private MoneybookDBManager dbManager;
    private EditText userID;
    private EditText userPW;
    private TextView okBtn;
    private TextView cancelBtn;
    private LoginService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbManager = new MoneybookDBManager(this);
        userID = (EditText) findViewById(R.id.login_id_edt);
        userPW = (EditText) findViewById(R.id.login_pw_edt);
        okBtn = (TextView) findViewById(R.id.login_ok_btn);
        cancelBtn = (TextView) findViewById(R.id.login_cancle_btn);

        final SharedPreferences prefs = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id_index = prefs.getInt("id_index", 0);
/*        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("id_index");
        editor.commit();*/
//        Log.d("LoginActivity의 id_index", String.valueOf(id_indexLong));
//        int id_index = 1;


        if (id_index != 0) {
            Intent intent = new Intent(LoginActivity.this, MoneyBookActivity.class);
            startActivity(intent);
            finish();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://moneybook25.cafe24.com/MoneyBookProject/android/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(LoginService.class);

            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = userID.getText().toString();
                    String pw = userPW.getText().toString();
                    Call<HashMap<String, Object>> call = service.login(id, pw);

                    call.enqueue(new Callback<HashMap<String, Object>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                            HashMap<String, Object> info = response.body();
                            String resultCode = info.get("result").toString();
                            if (resultCode.equals("2101")) {
                                dbManager.deleteAll(info.get("id_index").toString());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("id_index", Integer.parseInt(info.get("id_index").toString()));
                                editor.commit();
                                Toast.makeText(LoginActivity.this, "환영합니다!", Toast.LENGTH_SHORT).show();

                                List<MoneyBook> list = (List<MoneyBook>)info.get("moneybookList");
                                for (int i = 0; i < list.size(); i++) {
                                    //Gson 라이브러리를 사용하여 객체를 JSON으로 변환
                                    Gson gson = new Gson();
                                    String objJson = gson.toJson(list.get(i));
                                    Log.d("LoginActivity", objJson);

                                    //Gson 라이브러리를 사용하여 객체로부터 만들어진 JSON을 다시 객체로 변환
                                    MoneyBookTemp objFromJson = gson.fromJson(objJson, MoneyBookTemp.class);

                                    MoneyBook mb = new MoneyBook();
                                    mb.setMoneyBookNo(objFromJson.getMoneyBookNo());
                                    mb.setId_index(objFromJson.getId_index());
                                    mb.setCategory(objFromJson.getCategory());
                                    mb.setDetail(objFromJson.getDetail());
                                    mb.setPrice(objFromJson.getPrice());
                                    mb.setM_date(getDate(objFromJson.getDate(), "yyyy-MM-dd"));
                                    dbManager.insertMoneybook(mb);
                                }
                                Intent intent = new Intent(LoginActivity.this, MoneyBookActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (resultCode.equals("2102")) {
                                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public static Date getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        Date date = calendar.getTime();

        return date;
    }

}
