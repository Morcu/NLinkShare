package com.example.linkshare.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.linkshare.R;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    public int[] slide_images={
            R.drawable.link_share_logo,
            R.drawable.ready

    };

    public String[] slide_headings={ "Welcome to LinkShare" ,"Step 1/2","Step 2/2", "Ready to GO"};
    public String[] slide_descriptions={ ""
            ,"Follow the step 1 of <a href='https://developers.notion.com/docs/getting-started#step-1-create-an-integration'>https://developers.notion.com/docs/getting-started#step-1-create-an-integration</a> and paste the <b>internal integration token<b> here",
            "Follow the step 2 of <a href='https://developers.notion.com/docs/getting-started#step-2-share-a-database-with-your-integration'>https://developers.notion.com/docs/getting-started#step-2-share-a-database-with-your-integration</a> Share a database with your integration and paste the <b>Database ID<b>",
            "Start saving your links"};

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


        if (position == 0){
            slideImageView.setImageResource(slide_images[0]);
        }
        if (position == 3){
            slideImageView.setImageResource(slide_images[1]);
        }


        slideHeading.setText(Html.fromHtml(slide_headings[position]));
        slidedescription.setText(Html.fromHtml(slide_descriptions[position]));
        slidedescription.setMovementMethod(LinkMovementMethod.getInstance());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {

        container.removeView((RelativeLayout)object);

    }

}