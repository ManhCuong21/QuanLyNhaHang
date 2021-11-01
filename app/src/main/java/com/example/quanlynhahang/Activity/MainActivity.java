package com.example.quanlynhahang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.quanlynhahang.Adapter.SliderShowChaoAdapter;
import com.example.quanlynhahang.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;




public class MainActivity extends AppCompatActivity {

    SliderView sliderView;
    private int[] img = {R.drawable.man_hinh_chao_1
                        ,R.drawable.man_hinh_chao_2
                        ,R.drawable.man_hinh_chao_3
                        ,R.drawable.man_hinh_chao_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderView = findViewById(R.id.imgSliderShowChao);
        SliderShowChaoAdapter adapter = new SliderShowChaoAdapter(img);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

}
