/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.catgenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.agateau.utils.log.NLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {
    private static final String PARTS_JSON = "parts/parts.json";

    private final AvatarPartDb mAvatarPartDb = new AvatarPartDb();
    private long mSeed = 0;
    private EditText mNameEditText;
    private ImageView mImageView;
    private AvatarGenerator mAvatarGenerator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            InputStream stream = getAssets().open(PARTS_JSON);
            mAvatarPartDb.init(stream);
        } catch (IOException e) {
            NLog.e("Failed to open %s: %s", PARTS_JSON, e);
            throw new RuntimeException(e);
        }

        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);

        mNameEditText = findViewById(R.id.nameEditText);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSeed();
                generateAvatar();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        generateAvatar();

        ImageButton button = findViewById(R.id.shareButton);
        button.setOnClickListener(view -> shareAvatar());

        button = findViewById(R.id.infoButton);
        button.setOnClickListener(view -> showAbout());
    }

    private void generateAvatar() {
        int size = 1024;
        if (mAvatarGenerator != null) {
            mAvatarGenerator.cancel(true);
        }
        mAvatarGenerator = new AvatarGenerator(getAssets(), mAvatarPartDb, mImageView, size);
        mAvatarGenerator.execute(mSeed);
    }

    private void showAbout() {
        AboutActivity.start(this);
    }

    private void updateSeed() {
        String name = String.valueOf(mNameEditText.getText()).trim().toLowerCase();
        mSeed = 0;
        for (int idx = 0; idx < name.length(); ++idx) {
            mSeed += name.codePointAt(idx);
        }
    }

    private void shareAvatar() {
        File avatarDir = new File(getCacheDir(), "avatars");
        avatarDir.mkdirs();
        File avatarFile = new File(avatarDir, mSeed + ".png");

        if (!saveAvatar(avatarFile)) {
            return;
        }
        NLog.i("Avatar saved to %s", avatarFile);

        Uri contentUri = FileProvider.getUriForFile(this, "com.agateau.catgenerator.fileprovider", avatarFile);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_via)));
    }

    private boolean saveAvatar(File avatarFile) {
        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(avatarFile);
        } catch (FileNotFoundException e) {
            NLog.e("Opening %s failed: %s", avatarFile, e);
            return false;
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            stream.close();
        } catch (IOException e) {
            NLog.e("Failed to close %s: %s", avatarFile, e);
            return false;
        }
        return true;
    }
}
