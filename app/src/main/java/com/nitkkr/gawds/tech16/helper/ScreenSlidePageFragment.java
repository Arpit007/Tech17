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

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.Typewriter;

/**
 * Created by Karan on 20-12-2016.
 */

public class ScreenSlidePageFragment extends Fragment {
    int val;
    public static int[] images = {/*image ids go here*/};
    public static CharSequence[] headings = {/*text headings go here*/};
    public static String[] descriptions = {/*text descriptions go here*/};

    /**
     * the images here are applied directly
     * TODO: use functions to load images
     */
    public static ScreenSlidePageFragment init(int position) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.slide_layout, container, false);
        ImageView img = (ImageView) root.findViewById(R.id.slide_image);
        TextView tvd = (TextView) root.findViewById(R.id.description);

        Typewriter tvh = (Typewriter) root.findViewById(R.id.slide_heading);
        tvh.setCharacterDelay(80);
        tvh.animateText("   " + headings[val]);
        /**
         * change the image loading here
         */
        img.setImageResource(images[val]);

        return root;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            val = getArguments().getInt("pos");
        } catch (Exception e) {
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }

    }


}
