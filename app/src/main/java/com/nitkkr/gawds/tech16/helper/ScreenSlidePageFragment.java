package com.nitkkr.gawds.tech16.helper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Karan on 20-12-2016.
 */

public class ScreenSlidePageFragment extends Fragment {
    int val;
    public static int[] images = {R.drawable.food1,R.drawable.arrow,R.drawable.destination,R.drawable.check,R.drawable.user_name_image,R.drawable.food2,R.drawable.gender,R.drawable.number,};
    static ScreenSlidePageFragment init(int position) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("pos",position);
        f.setArguments(args);
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.slide_layout,container,false);
        ImageView img = (ImageView) root.findViewById(R.id.slide_image);
        TextView tvd = (TextView) root.findViewById(R.id.description);
        TextView tvh = (TextView) root.findViewById(R.id.slide_heading);
        img.setImageResource(images[val]);


        return root;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            val = getArguments().getInt("pos");
        }
        catch (Exception e) {
            Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
        }

    }


}
