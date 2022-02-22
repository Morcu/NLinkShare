package com.example.linkshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.linkshare.R;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }
    /*
    public int[] slide_images={
            R.drawable.cuisines,
            R.drawable.mealcourse,
            R.drawable.dishtype

    };
     */
    public String[] slide_headings={ "ONE" ,"TWO","THREE"};
    public String[] slide_descriptions={ "Select the Cuisine of the recipe that you desire, from numerous options starting from Indian and upto Greek."
            ,"Select the course of meal that you want.","Select the dish you want among widely categorized options."};

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;


    }
    public Object instantiateItem(ViewGroup container, int position){

        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView=(ImageView)view.findViewById(R.id.imageView2);
        TextView slideHeading=(TextView)view.findViewById(R.id.heading);
        TextView slidedescription=(TextView)view.findViewById(R.id.description);

        //slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slidedescription.setText(slide_descriptions[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {

        container.removeView((RelativeLayout)object);

    }

}