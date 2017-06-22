package com.mulcam.c901.yk.moneybookandroid.setting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    + "m_date integer)");
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

    public void insertPerson(MoneyBook mb) {
        ContentValues cv = new ContentValues();
        cv.put("moneyBookNo", mb.getMoneyBookNo());
        cv.put("id_index", mb.getId_index());
        cv.put("category", mb.getCategory());
        cv.put("detail", mb.getDetail());
        cv.put("price", mb.getPrice());
        cv.put("m_date", mb.getM_date());
        openHelper.getWritableDatabase().insert("moneybook", null, cv);
    }

    public void deletePerson(int moneyBookNo) {
        openHelper.getWritableDatabase().delete("moneybook", "moneyBookNo = ?", new String[]{moneyBookNo + ""});
    }

    public MoneyBook selectOne(int id_index) {
        Cursor cursor = openHelper.getReadableDatabase().rawQuery("select * from person where id_index=?", new String[]{id_index + ""});
        MoneyBook mb = null;
        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            mb = new MoneyBook();
            mb.setMoneyBookNo(cursor.getInt(0));
            mb.setId_index(cursor.getInt(1));
            mb.setCategory(cursor.getString(2));
            mb.setDetail(cursor.getString(3));
            mb.setPrice(cursor.getInt(4));
            mb.setM_date(cursor.getInt(5));
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
            mb.setM_date(cursor.getInt(5));
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
