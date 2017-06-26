package com.mulcam.c901.yk.moneybookandroid.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

            holder = new MoneybookViewHolder();
            holder.categoryImg = (ImageView) convertView.findViewById(R.id.day_list_category_image);
            holder.categoryTv = (TextView) convertView.findViewById(R.id.day_list_category_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.day_list_detail_tv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.day_list_price_tv);
            convertView.setTag(holder);
        } else {
            holder = (MoneybookViewHolder) convertView.getTag();
        }

        final MoneyBook mb = list.get(position);

        String categoryStr = mb.getCategory();
        holder.categoryTv.setText(categoryStr);
        holder.priceTv.setText(String.valueOf(mb.getPrice()));
        holder.detailTv.setText(mb.getDetail());
        String id = "R.drawable." + categoryStr;
        Log.d("drawable ID", id);
        int resID = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        holder.categoryImg.setImageBitmap(bitmap);

        return convertView;
    }
}
