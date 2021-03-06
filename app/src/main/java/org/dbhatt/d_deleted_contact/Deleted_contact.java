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

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devsb on 18-09-2016.
 */
public class Deleted_contact extends RecyclerView.Adapter<Deleted_contact.Contact> {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1433;
    private MainActivity mainActivity;
    private Context context;
    private boolean rtl = false;
    private Random rnd;
    private Paint paint;
    private ArrayList<org.dbhatt.d_deleted_contact.data.Contact> deleted_contact;

    Deleted_contact(ArrayList<org.dbhatt.d_deleted_contact.data.Contact> all_contact, Context context, MainActivity mainActivity) {
        try {
            this.deleted_contact = all_contact;
            rnd = new Random();
            paint = new Paint();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                rtl = context.getResources().getConfiguration().getLayoutDirection() != View.LAYOUT_DIRECTION_LTR;
            this.context = context;
            this.mainActivity = mainActivity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contact onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Contact(LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(Contact holder, int position) {
        try {
            org.dbhatt.d_deleted_contact.data.Contact contact = deleted_contact.get(position);
            holder.contact_name.setText(contact.getName());
            holder.account_type.setText(contact.getAccount_type());
            new Load_Contact_Photo(holder.contact_photo, contact.getName()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return deleted_contact.size();
    }

    class Contact extends RecyclerView.ViewHolder {

        CardView contact_raw;
        TextView contact_name, account_type;
        ImageView contact_photo;

        Contact(View itemView) {
            super(itemView);
            try {
                contact_raw = (CardView) itemView.findViewById(R.id.contact_raw);
                contact_name = (TextView) itemView.findViewById(R.id.contact_name);
                account_type = (TextView) itemView.findViewById(R.id.contact_account_type);
                contact_photo = (ImageView) itemView.findViewById(R.id.contact_photo);

                contact_raw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(deleted_contact.get(getAdapterPosition()).getName());
                            builder.setNegativeButton(R.string.restore, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (Build.VERSION.SDK_INT > 22) {
                                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                                restore_one_contact();
                                            } else {
                                                if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.WRITE_CONTACTS)) {
                                                    new AlertDialog.Builder(context)
                                                            .setTitle(R.string.permission)
                                                            .setMessage(R.string.permission_message_write_external_storage)
                                                            .setPositiveButton(R.string.permission_grant, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    ActivityCompat.requestPermissions(mainActivity,
                                                                            new String[]{Manifest.permission.WRITE_CONTACTS},
                                                                            REQUEST_WRITE_EXTERNAL_STORAGE);
                                                                }
                                                            }).create().show();
                                                } else {
                                                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                }
                                            }
                                        } else {
                                            restore_one_contact();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                            builder.create().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
            }
        }

        private void restore_one_contact() {
            /*   this function
                 recovers one selected contact
             */
            try {
                ArrayList<ContentProviderOperation> ops = new ArrayList();
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build())
                        .withSelection(ContactsContract.RawContacts._ID + "=?", new String[]{String.valueOf(deleted_contact.get(getAdapterPosition()).getId())})
                        .withValue(ContactsContract.RawContacts.DELETED, 0)
                        .withYieldAllowed(true)
                        .build());
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
            }
            try {
                mainActivity.add_to_all_contact(deleted_contact.get(getAdapterPosition()));
                deleted_contact.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (deleted_contact.size() == 0)
                    mainActivity.update_delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mainActivity.ask_for_ratings();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void delete_one_contact() {
            try {
                ArrayList<ContentProviderOperation> ops = new ArrayList();
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build())
                        .withSelection(ContactsContract.RawContacts._ID + "=?", new String[]{String.valueOf(deleted_contact.get(getAdapterPosition()).getId())})
                        .withValue(ContactsContract.RawContacts.DELETED, 1)
                        .withYieldAllowed(true)
                        .build());
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Load_Contact_Photo extends AsyncTask<Void, Void, Bitmap> {

        String contact_name;
        private final WeakReference<ImageView> imageViewReference;

        Load_Contact_Photo(ImageView imageView, String contact_name) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.contact_name = contact_name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Bitmap bitmap_photo = null;
                Canvas canvas = null;
                bitmap_photo = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap_photo);
                if (rtl)
                    contact_name = contact_name.substring(contact_name.length() - 1);
                else
                    contact_name = contact_name.substring(0, 1);
                paint.setStyle(Paint.Style.FILL);
                paint.setARGB(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
                canvas.drawPaint(paint);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(30);
                paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
                canvas.drawText(contact_name, 25, 35, paint);
                return bitmap_photo;
            } catch (Exception e) {
                e.printStackTrace();
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.developer);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            try {
                if (bitmap != null) {
                    final ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
            }
        }
    }
}