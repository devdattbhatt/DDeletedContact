
/*
 * Copyright (c) 2016. Devdatt s bhatt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.dbhatt.d_deleted_contact.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.R;

/**
 * Created by devsb on 18-09-2016.
 */
public class Contact_us extends AppCompatActivity implements View.OnClickListener {

    private static final int DO_NOT_FINISH_REQUEST_CODE = 143;
    private TextView facebook, whats_app, group_language, group_developer, linkedin, google_pluse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        setTitle(R.string.action_contact_us);
        setResult(DO_NOT_FINISH_REQUEST_CODE);

        whats_app = (TextView) findViewById(R.id.contact_to_whats_app);
        group_developer = (TextView) findViewById(R.id.contact_to_google_plus_developer);
        group_language = (TextView) findViewById(R.id.contact_to_language);
        google_pluse = (TextView) findViewById(R.id.contact_to_google_plus);
        facebook = (TextView) findViewById(R.id.contact_to_facebook);
        linkedin = (TextView) findViewById(R.id.contact_to_linkedin);

        whats_app.setOnClickListener(this);
        group_developer.setOnClickListener(this);
        group_language.setOnClickListener(this);
        google_pluse.setOnClickListener(this);
        facebook.setOnClickListener(this);
        linkedin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_to_whats_app:
                try {
                    startActivity(new Intent("android.intent.action.MAIN")
                            .setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"))
                            .putExtra("jid", PhoneNumberUtils.stripSeparators("919427159497") + "@s.whatsapp.net")
                            .setPackage("com.whatsapp"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(v, R.string.install_whats_app, Snackbar.LENGTH_SHORT);
                    snackbar.setAction(R.string.install, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //https://play.google.com/store/apps/details?id=com.whatsapp
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                        }
                    });
                    snackbar.show();
                }
                break;
            case R.id.contact_to_google_plus_developer:
                try {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://groups.google.com/forum/#!forum/dbhatt_org_android/join")), getString(R.string.share)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_to_language:
                try {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://groups.google.com/forum/#!forum/dbhatt_org_language/join")), getString(R.string.share)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_to_google_plus:
                try {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://plus.google.com/114708882354058631022")), getString(R.string.share)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_to_linkedin:
                try {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.linkedin.com/in/devdatt-bhatt-533136109")), getString(R.string.share)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_to_facebook:
                try {
                    if (getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode >= 3002850)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/dbhatt.org")));
                    else
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dbhatt.org")));
                } catch (Exception e) {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/dbhatt.org")), getString(R.string.share)));
                }
                break;
        }
    }
}
