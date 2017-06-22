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
import com.mulcam.c901.yk.moneybookandroid.service.LoginService;
import com.mulcam.c901.yk.moneybookandroid.setting.MoneybookDBManager;

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
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbManager = new MoneybookDBManager(this);
        userID = (EditText) findViewById(R.id.login_id_edt);
        userPW = (EditText) findViewById(R.id.login_pw_edt);
        okBtn = (TextView) findViewById(R.id.login_ok_btn);
        cancelBtn = (TextView) findViewById(R.id.login_cancle_btn);
        prefs = getSharedPreferences("loginInfo", MODE_PRIVATE);

        final SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String id_index = prefs.getString("id_index", "non_login");
        Toast.makeText(LoginActivity.this, id_index, Toast.LENGTH_SHORT).show();

//        test하려고 넣어놓은 소스..이따 지우자
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("id_index");
        editor.commit();

        if (!id_index.equals("non_login")) {
            Intent intent = new Intent(LoginActivity.this, MoneyBookActivity.class);
            startActivity(intent);
            finish();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.56.1:8080/MoneyBookProject/android/")
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
                                Toast.makeText(LoginActivity.this, "환영합니다!", Toast.LENGTH_SHORT).show();

                                List<MoneyBook> list = (List<MoneyBook>)info.get("moneybookList");
                                Toast.makeText(LoginActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < list.size(); i++) {
                                    //Gson 라이브러리를 사용하여 객체를 JSON으로 변환
                                    Gson gson = new Gson();
                                    String objJson = gson.toJson(list.get(i));
                                    Log.d("LoginActivity", objJson);

                                    //Gson 라이브러리를 사용하여 객체로부터 만들어진 JSON을 다시 객체로 변환
                                    MoneyBook objFromJson = gson.fromJson(objJson, MoneyBook.class);
                                    Log.d("LoginActivity", objFromJson.getCategory());
                                    Log.d("LoginActivity", String.valueOf(objFromJson.getM_date()));

                                    dbManager.insertPerson(objFromJson);

                                }

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("id_index", info.get("id_index").toString());
                                editor.commit();

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
}
