package com.mulcam.c901.yk.moneybookandroid.tab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mulcam.c901.yk.moneybookandroid.R;
import com.mulcam.c901.yk.moneybookandroid.service.MoneybookAddService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabDialog1 extends Fragment {

    private TextView dateTv;
    private Button cancelBtn;
    private Button addBtn;
    private Spinner spinner;
    private EditText content;
    private EditText price;
    private MoneybookAddService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_tab_dialog1, container, false);

        final int id_index = getArguments().getInt("id_index");

        dateTv = (TextView) rootView.findViewById(R.id.dateTv);
        cancelBtn = (Button) rootView.findViewById(R.id.cancelBtn);
        addBtn = (Button) rootView.findViewById(R.id.addBtn);
        spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        content = (EditText) rootView.findViewById(R.id.content);
        price = (EditText) rootView.findViewById(R.id.price);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.87.2:8080/MoneyBookProject/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MoneybookAddService.class);


        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinner.getSelectedItem().toString();
  /*              Toast.makeText(getActivity(),
                        category
                                +"," +dateTv.getText().toString()
                                + "," + content.getText().toString()
                                + "," + content.getText().toString() + "," + price.getText().toString()
                        , Toast.LENGTH_SHORT).show();*/

                Call<Integer> call = service.moneybookAddService(id_index,dateTv.getText().toString(),
                        category,content.getText().toString(),Integer.parseInt(price.getText().toString()));

                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Integer result = response.body();
                        if (result == 3201) {
                            Toast.makeText(getActivity(), "등록 성공", Toast.LENGTH_SHORT).show();
                        /*    Intent intent = new Intent(LoginActivity.this, MoneyBookActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else if (result == 3202) {
                            Toast.makeText(getActivity(), "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return rootView;
    }

}
