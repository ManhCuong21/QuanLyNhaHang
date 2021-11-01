package com.example.quanlynhahang.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quanlynhahang.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderShowChaoAdapter extends SliderViewAdapter<SliderShowChaoAdapter.Holder> {

    int[] img;

    public SliderShowChaoAdapter(int[] img) {
        this.img = img;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_chao,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.img.setImageResource(img[position]);
    }

    @Override
    public int getCount() {
        return img.length;
    }


    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView img;
        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgSliderChao);

        }
    }


}
