package com.agateau.catgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.agateau.utils.log.NLog;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mLinearLayout;

    private final PartDb mPartDb = new PartDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a LinearLayout in which to add the ImageView
        mLinearLayout = new LinearLayout(this);

        mPartDb.init(this);

        // Instantiate an ImageView and define its properties
        ImageView view = new ImageView(this);
        int id = mPartDb.getPart(PartDb.PART_NAMES[0], 1);
        view.setImageResource(id);
        view.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
        view.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT,
                Gallery.LayoutParams.WRAP_CONTENT));

        // Add the ImageView to the layout and set the layout as the content view
        mLinearLayout.addView(view);
        setContentView(mLinearLayout);
    }
}
