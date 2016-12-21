package com.nitkkr.gawds.tech16.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Dell on 22-Dec-16.
 */

public class ScreenSlidePageFragment extends Fragment {

    int val;
    public static int[] images = {R.drawable.user_bk};
    static ScreenSlidePageFragment init(int position) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("pos",position);
        f.setArguments(args);
        return f;
    }
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
            Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
        }

    }
}
