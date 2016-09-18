package org.dbhatt.ddeletedcontact.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dbhatt.ddeletedcontact.R;

/**
 * Created by devsb on 18-09-2016.
 */
public class Recycler_view extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);
        return view;
    }
}
