package com.agateau.catgenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.agateau.utils.log.NLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private final AvatarPartDb mAvatarPartDb = new AvatarPartDb();
    private long mSeed = 0;
    private EditText mNameEditText;
    private ImageView mImageView;
    private AvatarGenerator mAvatarGenerator = null;

    private final String PARTS_JSON = "parts/parts.json";

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
        mImageView = (ImageView) findViewById(R.id.imageView);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
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

        ImageButton button = (ImageButton) findViewById(R.id.shareButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAvatar();
            }
        });

        generateAvatar();
    }

    private void generateAvatar() {
        int size = 1024;
        if (mAvatarGenerator != null) {
            mAvatarGenerator.cancel(true);
        }
        mAvatarGenerator = new AvatarGenerator(this, mAvatarPartDb, mImageView, size);
        mAvatarGenerator.execute(mSeed);
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
        File avatarFile = new File(avatarDir, String.valueOf(mSeed) + ".png");

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

        startActivity(Intent.createChooser(shareIntent, "Share avatar with..."));
    }

    private boolean saveAvatar(File avatarFile) {
        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        FileOutputStream stream = null;
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
