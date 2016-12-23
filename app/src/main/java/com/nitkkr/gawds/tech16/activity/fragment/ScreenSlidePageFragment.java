package com.nitkkr.gawds.tech16.activity.fragment;

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
import com.nitkkr.gawds.tech16.helper.App;
import com.nitkkr.gawds.tech16.src.Typewriter;

/**
 * Created by Dell on 22-Dec-16.
 */

public class ScreenSlidePageFragment extends Fragment {

    int val;
    public static int[] images = {/*image ids go here*/R.drawable.about,R.drawable.events,R.drawable.guests,
            R.drawable.exh,R.drawable.info};
    public static CharSequence[] headings = {"About TS'17", "Events","Gusto Talk",
            "Exhibitions and Tech-Expo","Informals"};
    public static String[] descriptions = {"Come with techspardha as we launch for our latest adventure",
            "Along the way we will come across some exciting events",
            "Some experts will also join us and deliver their expertise",
            "We will come across some mind blowing and extraordinary XYZ",
            "descriptive content for Informals"};

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
        TextView tvh = (TextView) root.findViewById(R.id.slide_heading);

//        Typewriter tvh = (Typewriter) root.findViewById(R.id.slide_heading);
//        tvh.setCharacterDelay(80);
//        tvh.animateText("   " + headings[val]);
        tvh.setText(headings[val]);
        tvd.setText(descriptions[val]);

        img.setImageBitmap(App.decodeSampledBitmapFromResource(getResources(),images[val],500,500));

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
