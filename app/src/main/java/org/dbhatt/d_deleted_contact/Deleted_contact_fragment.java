package org.dbhatt.d_deleted_contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.Data.Contact;

import java.util.ArrayList;

/**
 * Created by dev on 4/10/16.
 */

public class Deleted_contact_fragment extends Fragment {

    ArrayList<Contact> deleted_contact;
    RecyclerView recyclerView;
    TextView data_not_found;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.adapter_recycler_view);
        data_not_found = (TextView) view.findViewById(R.id.no_data_found);

        update();
        return view;
    }

    public void update() {
        deleted_contact = ((MainActivity) getActivity()).get_deleted_contact();
        if (deleted_contact.size() > 0) {
            data_not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new Deleted_contact(deleted_contact, getContext(), ((MainActivity) getActivity())));
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            data_not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
