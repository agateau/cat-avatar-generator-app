package com.agateau.catgenerator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.agateau.utils.NTemplate;

public class AboutActivity extends AppCompatActivity {
    private static final String AUTHOR_EMAIL = "mail@agateau.com";
    private static final String GPLAY_URL = "https://play.google.com/store/apps/details?id=com.agateau.catgenerator";
    private static final String LICENSE_URL = "http://www.apache.org/licenses/";
    private static final String PROJECT_URL = "https://github.com/agateau/cat-avatar-generator-app";
    private static final String CATGEN_URL = "http://www.peppercarrot.com/extras/html/2016_cat-generator/index.php";
    private static final String VERSION_NAME = "0.1.0";

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        NTemplate template = new NTemplate(getText(R.string.about_description));
        template.put("email", AUTHOR_EMAIL)
                .put("rate_url", GPLAY_URL)
                .put("catgen_url", CATGEN_URL)
                .put("license_url", LICENSE_URL)
                .put("project_url", PROJECT_URL);

        TextView view = (TextView) findViewById(R.id.descriptionTextView);
        view.setText(template.toSpanned());
        view.setMovementMethod(LinkMovementMethod.getInstance());

        view = (TextView) findViewById(R.id.versionTextView);
        view.setText(VERSION_NAME);
    }
}
