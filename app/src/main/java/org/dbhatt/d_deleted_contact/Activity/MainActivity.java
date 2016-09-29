package org.dbhatt.d_deleted_contact.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.dbhatt.d_deleted_contact.Data.Contact;
import org.dbhatt.d_deleted_contact.Fragment.All_contact;
import org.dbhatt.d_deleted_contact.Fragment.Deleted_contact;
import org.dbhatt.d_deleted_contact.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int DO_NOT_FINISH_REQUEST_CODE = 143, APP_INVITE = 9211;
    private static boolean refreshing = false;
    private static ContentResolver contentResolver;
    private static int READ_CONTACT_PERMISSION_REQUEST = 0000;
    private boolean finish_activity = true;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<Contact> all_contact, deleted_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        finish_activity = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(getApplicationContext(), Splash.class), DO_NOT_FINISH_REQUEST_CODE);
            }
        }).start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        all_contact = new ArrayList<>();
        deleted_contact = new ArrayList<>();

        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        load_contacts();
                    }
                }).start();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            READ_CONTACT_PERMISSION_REQUEST);
                } else
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISSION_REQUEST);

            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    load_contacts();
                }
            }).start();
        }
    }

    private void load_contacts() {
        contentResolver = getContentResolver();
        new Update_lists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restore_all:
                if (deleted_contact.isEmpty())
                    Toast.makeText(this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_app_invite:
                finish_activity = false;
                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(AppInvite.API)
                        .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        }).build();

                Intent app_invite_intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invite_title))
                        .setMessage(getString(R.string.invite_message))
                        .setEmailSubject(getString(R.string.invite_email_subject))
                        .setEmailHtmlContent(getString(R.string.invite_html_contact))
                        .build();
                startActivityForResult(app_invite_intent, APP_INVITE);
                break;

            case R.id.action_share:

                break;

            case R.id.action_contact_us:
                finish_activity = false;
                startActivityForResult(new Intent(getApplicationContext(), Contact_us.class), DO_NOT_FINISH_REQUEST_CODE);
                break;
            case R.id.action_refresh:
                if (refreshing)
                    Toast.makeText(this, R.string.try_after_some_time, Toast.LENGTH_SHORT).show();
                else new Update_lists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        switch (requestCode) {
            case DO_NOT_FINISH_REQUEST_CODE:
                finish_activity = true;
                break;
            case APP_INVITE:
                finish_activity = true;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (new File(Environment.getExternalStorageDirectory(), "tmp.png").exists())
            new File(Environment.getExternalStorageDirectory(), "tmp.png").delete();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new All_contact();
                case 1:
                    return new Deleted_contact();
                default:
                    return new All_contact();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.all_contact);
                case 1:
                    return getString(R.string.deleted_contact);
            }
            return null;
        }
    }


    class Update_lists extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            refreshing = true;
            deleted_contact.clear();
            all_contact.clear();
            try {
                Cursor cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, null, "DISPLAY_NAME IS NOT NULL", null, "display_name COLLATE LOCALIZED ASC");
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    while (cursor.moveToNext()) {
                        if (cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts.DELETED)) == 1) {
                            deleted_contact.add(new Contact(cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts._ID)),
                                    cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)),
                                    cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)),
                                    cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)),
                                    "1"));
                        } else {
                            all_contact.add(new Contact(cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts._ID)),
                                    cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)),
                                    cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)),
                                    cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)),
                                    "0"));
                        }
                    }
                }
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
                Log.e("all data", String.valueOf(all_contact.size()));
                Log.e("deleted data", String.valueOf(deleted_contact.size()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshing = false;
        }

    }

    public ArrayList<Contact> get_all_contact() {
        return all_contact;
    }

    public ArrayList<Contact> get_deleted_contact() {
        return deleted_contact;
    }

    public void setFinish_activity(boolean finish_activity) {
        this.finish_activity = finish_activity;
    }
}
