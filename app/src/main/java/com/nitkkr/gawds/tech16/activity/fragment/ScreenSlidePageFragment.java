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
    public static CharSequence[] headings = {"About TS'17", "Events","Guest Lectures",
            "Events","Informals"};
    public static String[] descriptions = {"Techspardha is the Techno-managerial festival of NIT Kurukshetra which started in 1995 as" +
            "“Technospect” (later changed to Literati).",
            "descriptive content for Events 1", "With immense pleasure techsaprdha’17 presents, guest lecture series of different scholars in the field"
            + "of literature, technology and many others.",
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
        TextView tvh = (TextView) root.findViewById(R.id.slide_heading);

//        Typewriter tvh = (Typewriter) root.findViewById(R.id.slide_heading);
//        tvh.setCharacterDelay(80);
//        tvh.animateText("   " + headings[val]);
        tvh.setText(headings[val]);
        tvd.setText(descriptions[val]);

        img.setImageBitmap(App.decodeSampledBitmapFromResource(getResources(),images[val],500,500));
        //img.setImageDrawable(getResources().getDrawable(images[val]));

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
