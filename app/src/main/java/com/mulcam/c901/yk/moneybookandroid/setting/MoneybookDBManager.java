package com.mulcam.c901.yk.moneybookandroid.setting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mulcam.c901.yk.moneybookandroid.calendar.MonthItem;
import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by student on 2017-06-21.
 */

public class MoneybookDBManager {
    class MoneybookDBOpenHelper extends SQLiteOpenHelper {
        public MoneybookDBOpenHelper(Context context, int version) {
            super(context, "moneybook_db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //테이블 생성
            db.execSQL("create table if not exists moneybook("
                    + "moneyBookNo integer primary key,"
                    + "id_index integer,"
                    + "category text, "
                    + "detail text, "
                    + "price integer, "
                    + "m_date text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists moneybook");
            onCreate(db);

        }
    }

    private MoneybookDBOpenHelper openHelper;
    private Context context;
    private static int DB_VERSION = 1;

    public MoneybookDBManager(Context context) {
        this.context = context;
        openHelper = new MoneybookDBOpenHelper(context, DB_VERSION);
    }

    public void insertMoneybook(MoneyBook mb) {
        ContentValues cv = new ContentValues();
        cv.put("moneyBookNo", mb.getMoneyBookNo());
        cv.put("id_index", mb.getId_index());
        cv.put("category", mb.getCategory());
        cv.put("detail", mb.getDetail());
        cv.put("price", mb.getPrice());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(mb.getM_date());
        cv.put("m_date", dateStr);
        openHelper.getWritableDatabase().insert("moneybook", null, cv);
    }

    public void updateMoneybook(MoneyBook mb) {
        ContentValues cv = new ContentValues();
        cv.put("moneyBookNo", mb.getMoneyBookNo());
        cv.put("id_index", mb.getId_index());
        cv.put("category", mb.getCategory());
        cv.put("detail", mb.getDetail());
        cv.put("price", mb.getPrice());
        cv.put("m_date", mb.getM_date().toString());

        openHelper.getWritableDatabase().update("moneybook", cv, "moneyBookNo=?", new String[]{mb.getMoneyBookNo() + ""});
    }

    public void deleteMoneybook(int moneyBookNo) {
        openHelper.getWritableDatabase().delete("moneybook", "moneyBookNo = ?", new String[]{moneyBookNo + ""});
    }

    public void deleteAll(String id_index) {
        openHelper.getWritableDatabase().delete("moneybook", "id_index = ?", new String[]{id_index});
    }

    public List<MoneyBook> selectMoneyBookList(int id_index) {
        Cursor cursor = openHelper.getReadableDatabase().rawQuery("select * from moneybook where id_index = ?", new String[]{id_index + ""});
        List<MoneyBook> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoneyBook mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            String mdateStr = cursor.getString(5);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(mdateStr);
                mb.setM_date(date);
            } catch (ParseException e) {
                mb.setM_date(null);
                Log.e("LoginActivity", "데이트 포맷이 맞지 않습니다(selectAll)");
            }
            list.add(mb);
        }
        return list;
    }

    public List<MoneyBook> selectDayList(int id_index, Date curDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = format.format(curDate);
        Cursor cursor = openHelper.getReadableDatabase().rawQuery("select * from moneybook where id_index = ? and m_date = ?", new String[]{id_index + "", date+""});
        List<MoneyBook> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoneyBook mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            String mdateStr = cursor.getString(5);
            try {
                Date m_date = format.parse(mdateStr);
                mb.setM_date(m_date);
            } catch (ParseException e) {
                mb.setM_date(null);
                Log.e("LoginActivity", "데이트 포맷이 맞지 않습니다(selectAll)");
            }
            list.add(mb);
        }
        return list;
    }

    public MoneyBook selectOne(int moneybookNo) {
        Cursor cursor = openHelper.getReadableDatabase().rawQuery("select * from moneybook where moneyBookNo = ?", new String[]{moneybookNo + ""});
        MoneyBook mb = null;
        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            String mdateStr = cursor.getString(5);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(mdateStr);
                mb.setM_date(date);
            } catch (ParseException e) {
                mb.setM_date(null);
                Log.e("LoginActivity", "데이트 포맷이 맞지 않습니다(selectAll)");
            }
        }
        return mb;
    }

    public List<MoneyBook> selectAll() {
        Cursor cursor = openHelper.getReadableDatabase().query("moneybook", new String[]{"moneyBookNo", "id_index", "category", "detail", "price", "m_date"}
                , null, null, null, null, null, null);
        //테이블명을 조회할 컬럼명 리스트, 조건절, 조건절에 들어갈 값들
        //헤빙절, 그룹바이절, 오더바이절, 리미트

        List<MoneyBook> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoneyBook mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            String mdateStr = cursor.getString(5);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(mdateStr);
                mb.setM_date(date);
            } catch (ParseException e) {
                mb.setM_date(null);
                Log.e("LoginActivity", "데이트 포맷이 맞지 않습니다(selectAll)");
            }
        }
        return list;
    }

    public List<MoneyBook> selectOneMonth() {
        Cursor cursor = openHelper.getReadableDatabase().query("moneybook", new String[]{"moneyBookNo", "id_index", "category", "detail", "price", "m_date"}
                , null, null, null, null, null, null);
        //테이블명을 조회할 컬럼명 리스트, 조건절, 조건절에 들어갈 값들
        //헤빙절, 그룹바이절, 오더바이절, 리미트

        List<MoneyBook> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoneyBook mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            String mdateStr = cursor.getString(5);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(mdateStr);
                mb.setM_date(date);
            } catch (ParseException e) {
                mb.setM_date(null);
                Log.e("LoginActivity", "데이트 포맷이 맞지 않습니다(selectAll)");
            }
        }
        return list;
    }

    public Cursor getCursor() {
        Cursor cursor = openHelper.getReadableDatabase().query(
                "moneybook"
                , new String[]{"moneyBookNo", "id_index", "category", "detail", "price", "m_date"}
                , null, null, null, null, null, null);
        return cursor;
    }

}
