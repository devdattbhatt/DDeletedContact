package org.dbhatt.d_deleted_contact.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.Activity.MainActivity;
import org.dbhatt.d_deleted_contact.Data.Contact;
import org.dbhatt.d_deleted_contact.R;

import java.util.ArrayList;

/**
 * Created by dev on 4/10/16.
 */

public class All_contact extends Fragment {

    View view;
    ArrayList<Contact> all_contact;
    RecyclerView recyclerView;
    TextView data_not_found;
    RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        all_contact = ((MainActivity) getActivity()).get_all_contact();
        view = inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.adapter_recycler_view);
        data_not_found = (TextView) view.findViewById(R.id.no_data_found);

        if (all_contact.size() > 0) {
            data_not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new org.dbhatt.d_deleted_contact.Data.All_contact(all_contact, getContext(), ((MainActivity) getActivity()));
            recyclerView.setAdapter(adapter);
        } else {
            data_not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return view;
    }

    public void update() {
        all_contact = ((MainActivity) getActivity()).get_all_contact();
        if (all_contact.size() > 0) {
            data_not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new org.dbhatt.d_deleted_contact.Data.All_contact(all_contact, getContext(), ((MainActivity) getActivity())));
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            data_not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void add_contact(Contact contact) {
        if (all_contact.size() > 0) {
            all_contact.add(0, contact);
            adapter.notifyItemInserted(0);
        } else {
            data_not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new org.dbhatt.d_deleted_contact.Data.All_contact(all_contact, getContext(), ((MainActivity) getActivity())));
            recyclerView.setNestedScrollingEnabled(false);
        }
    }
}
