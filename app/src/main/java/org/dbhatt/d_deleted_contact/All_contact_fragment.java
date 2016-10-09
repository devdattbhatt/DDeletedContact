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

package org.dbhatt.d_deleted_contact;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.dbhatt.d_deleted_contact.Data.Contact;

import java.util.ArrayList;

/**
 * Created by dev on 4/10/16.
 */

public class All_contact_fragment extends Fragment {

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
            adapter = new All_contact(all_contact, getContext(), ((MainActivity) getActivity()));
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
            recyclerView.setAdapter(new All_contact(all_contact, getContext(), ((MainActivity) getActivity())));
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            data_not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void add_contact(Contact contact) {
        try {
            Cursor cursor = getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts._ID,
                            ContactsContract.RawContacts.CONTACT_ID,
                            ContactsContract.RawContacts.ACCOUNT_TYPE,
                            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                    ContactsContract.RawContacts._ID + "=?",
                    new String[]{String.valueOf(contact.getId())},
                    null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Toast.makeText(getContext(), String.valueOf(cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID))), Toast.LENGTH_SHORT).show();
                    all_contact.add(0, new Contact(cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts._ID)),
                            cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE))));
                }
                if (!cursor.isClosed())
                    cursor.close();
            } else {
                all_contact.add(0, contact);
            }
            adapter.notifyItemInserted(0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
