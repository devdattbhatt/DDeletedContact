package org.dbhatt.d_deleted_contact.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.dbhatt.d_deleted_contact.R;

/**
 * Created by devsb on 18-09-2016.
 */
public class Splash extends AppCompatActivity {
    private static final int DO_NOT_FINISH_REQUEST_CODE = 143;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                setResult(DO_NOT_FINISH_REQUEST_CODE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onPause();
            }
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
