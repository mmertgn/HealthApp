package com.example.healthapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    SliderAdapter(Context context) {
        this.context = context;
    }

    private int[] slide_images = new int[]{
            R.drawable.surgeon,
            R.drawable.medicine,
            R.drawable.medical_history
    };

    private String[] slide_headings = {
            "Akıllı Diyabetim",
            "Uygulama Hakkında",
            "Başlık 3"
    };

    private String[] slide_decs = {
            "Bu uygulama diyabet aplikasyonu diyabetli bireyin günlük hayatını yönetebilmesi için\n" +
                    "rehber niteliğindedir.\n\n",
            "Bireyin temel ihtiyaçları hakkında profesyonel öneriler ve sağlık " +
                    "kaydı tutulması olanaklarını kullanıcılarına sağlar.\n\n",
            "Lorem Ipsum, dizgi ve baskı endüstrisinde kullanılan mıgır metinlerdir.1960'larda Lorem Ipsum pasajları da içeren Letraset yapraklarının yayınlanması ile ve yakın zamanda Aldus PageMaker gibi Lorem Ipsum sürümleri içeren masaüstü yayıncılık yazılımları ile popüler olmuştur."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView mImgSlidePic = view.findViewById(R.id.imgSlidePic);
        TextView mTxtSlideHeading = view.findViewById(R.id.txtSlideHeading);
        TextView mTxtSlideDesc = view.findViewById(R.id.txtSlideDesc);

        mImgSlidePic.setImageResource(slide_images[position]);
        mTxtSlideHeading.setText(slide_headings[position]);
        mTxtSlideDesc.setText(slide_decs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
