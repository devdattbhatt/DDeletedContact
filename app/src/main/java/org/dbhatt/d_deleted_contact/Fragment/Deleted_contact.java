package org.dbhatt.d_deleted_contact.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.Activity.MainActivity;
import org.dbhatt.d_deleted_contact.Data.Contact;
import org.dbhatt.d_deleted_contact.R;

import java.util.ArrayList;

/**
 * Created by devsb on 18-09-2016.
 */
public class Deleted_contact extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Contact> deleted_contact;
        RecyclerView recyclerView;
        View view;


        deleted_contact = ((MainActivity) getActivity()).get_deleted_contact();
        Log.e("all contact", String.valueOf(deleted_contact.size()));
        if (deleted_contact.size() > 0) {
            view = inflater.inflate(R.layout.recyclerview, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.adapter_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new org.dbhatt.d_deleted_contact.Data.Deleted_contact(deleted_contact, getContext()));
            recyclerView.setNestedScrollingEnabled(false);
            return view;
        } else {
            view = inflater.inflate(R.layout.no_data_found, container, false);
            TextView dbhatt_org = (TextView) view.findViewById(R.id.dbhatt_org);
            dbhatt_org.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dbhatt.org")));
                }
            });
            return view;
        }
    }
}