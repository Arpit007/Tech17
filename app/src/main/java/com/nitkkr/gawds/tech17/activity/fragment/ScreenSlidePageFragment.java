package com.nitkkr.gawds.tech17.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.App;

/**
 * Created by Dell on 22-Dec-16.
 */

public class ScreenSlidePageFragment extends Fragment
{

	int val;
	public static int[] images = {/*image ids go here*/R.drawable.about, R.drawable.events, R.drawable.guests,
			R.drawable.exh, R.drawable.info };
	public static CharSequence[] headings = { "About TS '17", "Events", "Gusto Talk",
			"Exhibitions and Tech-Expo", "Informals" };
	public static String[] descriptions = { " Fasten your seatbelts as we launch into the universe of Techspardha.",
			" A plethora of events awaits you in this journey.",
			" Learn from the best of the best through our guest lectures.",
			" See the brightest innovations of modern world in the exhibitions.",
			" We have made this journey as much fun as possible. Come, meet us at informalz." };

	public static ScreenSlidePageFragment init(int position)
	{
		ScreenSlidePageFragment f = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt("pos", position);
		f.setArguments(args);
		return f;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.slide_layout, container, false);
		ImageView img = (ImageView) root.findViewById(R.id.slide_image);
		TextView tvd = (TextView) root.findViewById(R.id.description);
		TextView tvh = (TextView) root.findViewById(R.id.slide_heading);

//        Typewriter tvh = (Typewriter) root.findViewById(R.id.slide_heading);
//        tvh.setCharacterDelay(80);
//        tvh.animateText("   " + headings[val]);
		tvh.setText(headings[val]);
		tvd.setText(descriptions[val]);

		img.setImageBitmap(App.decodeSampledBitmapFromResource(getResources(), images[val], 500, 500));

		return root;
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			val = getArguments().getInt("pos");
		}
		catch (Exception e)
		{
			Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
		}

	}

}
