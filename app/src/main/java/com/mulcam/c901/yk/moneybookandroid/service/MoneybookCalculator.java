package com.mulcam.c901.yk.moneybookandroid.service;

import android.os.AsyncTask;

import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;
import com.mulcam.c901.yk.moneybookandroid.setting.MoneybookDBManager;

import java.util.List;

/**
 * Created by student on 2017-06-22.
 */

public class MoneybookCalculator {
    private static MoneybookCalculator calculator;

    private MoneybookCalculator() {
    }

    public MoneybookCalculator getInstance() {
        if (calculator == null)
            calculator = new MoneybookCalculator();
        return calculator;
    }


}
