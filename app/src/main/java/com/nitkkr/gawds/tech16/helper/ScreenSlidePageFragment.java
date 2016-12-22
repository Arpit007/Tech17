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
    public static int[] images = {/*image ids go here*/R.drawable.about,R.drawable.events,R.drawable.guests,
            R.drawable.exh,R.drawable.info};
    public static CharSequence[] headings = {/*text headings go here*/"About", "Events","Guest Lectures",
            "Events","Informals"};
    public static String[] descriptions = {/*text descriptions go here*/"descriptive content for About",
            "descriptive content for Events 1", "descriptive content for guest lectures",
            "descriptive content for events 2", "descriptive content for Informals"};

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

        tvd.setText(descriptions[val]);

        img.setImageBitmap(App.decodeSampledBitmapFromResource(getResources(),images[val],100,100));

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
