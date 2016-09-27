package org.dbhatt.d_deleted_contact.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.R;

/**
 * Created by devsb on 18-09-2016.
 */
public class Contact_us extends AppCompatActivity implements View.OnClickListener {

    private static boolean finish_activity = false;
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
                finish_activity = false;
                startActivityForResult(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:9427159497")).setPackage("com.whatsapp"), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.contact_to_google_plus_developer:
                finish_activity = false;
                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://groups.google.com/forum/#!forum/dbhatt_org_android/join")), getString(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.contact_to_language:
                finish_activity = false;
                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://groups.google.com/forum/#!forum/dbhatt_org_language/join")), getString(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.contact_to_google_plus:
                finish_activity = false;
                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://plus.google.com/114708882354058631022")), getString(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.contact_to_linkedin:
                finish_activity = false;
                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.linkedin.com/in/devdatt-bhatt-533136109")), getString(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.contact_to_facebook:
                finish_activity = false;
                try {
                    if (getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode >= 3002850)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/dbhatt.org")));
                    else
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dbhatt.org")));
                } catch (Exception e) {
                    startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/dbhatt.org")), getString(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (finish_activity)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DO_NOT_FINISH_REQUEST_CODE)
            finish_activity = true;
    }
}
