package com.nitkkr.gawds.tech16.Src;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dell on 15-Dec-16.
 */

public class MyTextview extends TextView {

    public MyTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyTextview(Context context) {
        super(context);
        init();
    }

    public MyTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            setTypeface(tf);
        }
    }
}

