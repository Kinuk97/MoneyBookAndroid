package com.mulcam.c901.yk.moneybookandroid.calendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mulcam.c901.yk.moneybookandroid.R;
import com.mulcam.c901.yk.moneybookandroid.model.MoneyBook;

import java.util.List;

/**
 * Created by student on 2017-06-23.
 */

public class MoneybookAdapter extends ArrayAdapter<MoneyBook> {
    private Context context;
    private int resource;
    private List<MoneyBook> list;

    public MoneybookAdapter(@NonNull Context context,
                         @LayoutRes int resource,
                         @NonNull List<MoneyBook> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    public List<MoneyBook> getList() {
        return list;
    }

    static class MoneybookViewHolder {
        public TextView categoryTv;
        public TextView priceTv;
        public TextView detailTv;
        public ImageView categoryImg;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //1. inflation
        //2. findviewbyid
        //3. data바인딩

        MoneybookViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);

            /*tv = (TextView) convertView.findViewById(R.id.item_contact_name);
            convertView.setTag(100, tv);*/

            //holder 객체를 만들어서 findviewByid한 내용을 세팅하고
            holder = new MoneybookViewHolder();
            holder.categoryImg = (ImageView) convertView.findViewById(R.id.day_list_category_image);
            holder.categoryTv = (TextView) convertView.findViewById(R.id.day_list_category_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.day_list_detail_tv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.day_list_price_tv);
            //태그로 한번에 박았다가
            convertView.setTag(holder);
        } else {
            //findviewbyid를 매번 하지 않아도 되어 성능이 좋아진다
           /* tv = (TextView) convertView.getTag(100);*/

            //재사용할땐 한번에 회수
            holder = (MoneybookViewHolder) convertView.getTag();
        }

        //data 바인딩
        final MoneyBook mb = list.get(position);

//        이미지 박기 보류
//        holder.categoryImg.setImageDrawable();
        holder.categoryTv.setText(mb.getCategory());
        holder.priceTv.setText(mb.getPrice());
        holder.detailTv.setText(mb.getDetail());
/*        int gender = person.getGender();
        if (gender % 2 == 1) {
            holder.profileImg.setImageResource(R.drawable.user);
        } else {
            holder.profileImg.setImageResource(R.drawable.user_women);
        }

        holder.callImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + person.getPhone()));
                context.startActivity(intent);

//                이 퍼미션을 추가한 후 진짜 핸드폰에서 실행하면 바로 전화 걸림
//                <uses-permission android:name="android.permission.CALL_PHONE"/>
            }
        });*/


        return convertView;

//        return super.getView(position, convertView, parent);
    }
}
