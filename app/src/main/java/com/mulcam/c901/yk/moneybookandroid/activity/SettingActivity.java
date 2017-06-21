package com.mulcam.c901.yk.moneybookandroid.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mulcam.c901.yk.moneybookandroid.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by kiuka on 2017-06-21.
 */

public class SettingActivity extends PreferenceActivity {
    private GregorianCalendar calendar = new GregorianCalendar();

    private int hour = calendar.get(Calendar.HOUR_OF_DAY);

    private int minute = calendar.get(Calendar.MINUTE);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_pref);
        // 이거는 나중에 해봐야할 듯
        // setContentView(R.layout.testtest);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();

        if (key != null) {
            if (key.equals("set_time")) {
                // 시간 설정 다이얼로그 생성
                new TimePickerDialog(SettingActivity.this, timeSetListener, hour, minute, false).show();
            } else if (key.equals("reset")) {
                // 초기화하는 부분
            }
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    // 알림 시간 다이얼로그 set리스너
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String msg = String.format("%d / %d", hourOfDay, minute);
            String msg = hourOfDay + "-" + minute;

            //기본 SharedPreference를 가져옴. (PreferenceActivity에서 설정한 pref)
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
            //Preference 자료 수정을 위하여 editor 생성
            SharedPreferences.Editor edit = sharedPref.edit();
            //"set_term" 키의 값을 원하는 string으로 변경
            edit.putString("notification_time", msg);
            //변경된 값을 저장한다.
            edit.commit();

            String result = sharedPref.getString("notification_time", "실패");

            Toast.makeText(SettingActivity.this, result, Toast.LENGTH_LONG).show();
        }
    };


}
