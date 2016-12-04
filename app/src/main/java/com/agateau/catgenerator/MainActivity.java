package com.agateau.catgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mLinearLayout;

    private final PartDb mPartDb = new PartDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPartDb.init(this);

        setContentView(R.layout.activity_main);
        mLinearLayout = new LinearLayout(this);
        AvatarView view = new AvatarView(mPartDb, this);
        view.setSeed(256);
        mLinearLayout.addView(view);
        setContentView(mLinearLayout);
    }
}
