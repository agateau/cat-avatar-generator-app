package com.agateau.catgenerator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.agateau.utils.log.NLog;

public class MainActivity extends AppCompatActivity {
    private final PartDb mPartDb = new PartDb();
    private EditText mNameEditText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPartDb.init(this);

        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateAvatar();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        generateAvatar();
    }

    private void generateAvatar() {
        String name = String.valueOf(mNameEditText.getText());
        long seed = 0;
        for (int idx = 0; idx < name.length(); ++idx) {
            seed += name.codePointAt(idx);
        }
        NLog.i("name=%s seed=%d", name, seed);
        int size = 1024;
        Bitmap bitmap = AvatarGenerator.generate(this, mPartDb, seed, size);
        mImageView.setImageBitmap(bitmap);
    }
}
