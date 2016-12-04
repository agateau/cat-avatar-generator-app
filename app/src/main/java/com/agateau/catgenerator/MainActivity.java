package com.agateau.catgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.agateau.utils.log.NLog;

public class MainActivity extends AppCompatActivity {
    private final PartDb mPartDb = new PartDb();
    private AvatarView mAvatarView;
    private EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPartDb.init(this);

        setContentView(R.layout.activity_main);
        mAvatarView = new AvatarView(mPartDb, this);
        mAvatarView.setSeed(256);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rootLayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ABOVE, R.id.nameTextView);
        layout.addView(mAvatarView, params);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSeed();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateSeed() {
        String name = String.valueOf(mNameEditText.getText());
        long seed = 0;
        for (int idx = 0; idx < name.length(); ++idx) {
            seed += name.codePointAt(idx);
        }
        NLog.i("name=%s seed=%d", name, seed);
        mAvatarView.setSeed(seed);
    }
}
