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
    private static final String CODE_LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
    private static final String ART_LICENSE_URL = "https://creativecommons.org/licenses/by/4.0/";
    private static final String PROJECT_URL = "https://github.com/agateau/cat-avatar-generator-app";
    private static final String CATGEN_URL = "http://www.peppercarrot.com/extras/html/2016_cat-generator/index.php";
    private static final String VERSION_NAME = "0.2.0";

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
                .put("gplay_url", GPLAY_URL)
                .put("catgen_url", CATGEN_URL)
                .put("code_license_url", CODE_LICENSE_URL)
                .put("art_license_url", ART_LICENSE_URL)
                .put("project_url", PROJECT_URL);

        TextView view = (TextView) findViewById(R.id.descriptionTextView);
        view.setText(template.toSpanned());
        view.setMovementMethod(LinkMovementMethod.getInstance());

        view = (TextView) findViewById(R.id.versionTextView);
        view.setText(VERSION_NAME);
    }
}
