package org.dbhatt.d_deleted_contact.Data;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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

import org.dbhatt.d_deleted_contact.Activity.MainActivity;
import org.dbhatt.d_deleted_contact.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devsb on 18-09-2016.
 */
public class All_contact extends RecyclerView.Adapter<All_contact.Contact> {

    private static final int DO_NOT_FINISH_REQUEST_CODE = 143,
            REQUEST_READ_CONTACTS_CONTACT = 1431;
    private MainActivity mainActivity;
    private Context context;
    private boolean rtl = false;
    private Random rnd;
    private Paint paint;
    private ContentResolver resolver;
    private ArrayList<org.dbhatt.d_deleted_contact.Data.Contact> all_contact;

    public All_contact(ArrayList<org.dbhatt.d_deleted_contact.Data.Contact> all_contact, Context context, MainActivity mainActivity) {
        try {
            this.all_contact = all_contact;
            resolver = context.getContentResolver();
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
        return new Contact(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false));
    }

    @Override
    public void onBindViewHolder(Contact holder, int position) {
        try {
            org.dbhatt.d_deleted_contact.Data.Contact contact = all_contact.get(position);
            holder.contact_name.setText(contact.getName());
            holder.account_type.setText(contact.getAccount_type());
            new Load_Contact_Photo(holder.contact_photo, contact.getName()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(contact.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return all_contact.size();
    }

    class Contact extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView contact_raw;
        TextView contact_name, account_type;
        ImageView contact_photo, contact_info;

        Contact(View itemView) {
            super(itemView);

            try {
                contact_raw = (CardView) itemView.findViewById(R.id.contact_raw);
                contact_name = (TextView) itemView.findViewById(R.id.contact_name);
                account_type = (TextView) itemView.findViewById(R.id.contact_account_type);
                contact_photo = (ImageView) itemView.findViewById(R.id.contact_photo);
                contact_info = (ImageView) itemView.findViewById(R.id.contact_info);

                contact_raw.setOnClickListener(this);
                contact_info.setOnClickListener(this);
                contact_photo.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.contact_raw:

                    break;
                case R.id.contact_photo:
                    try {
                        final Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DATA15},
                                ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " =?",
                                new String[]{String.valueOf(all_contact.get(getAdapterPosition()).getId()), android.provider.ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}
                                , null);
                        if (cursor != null)
                            if (cursor.moveToFirst()) {
                                final byte[] photo = cursor.getBlob(0);
                                cursor.close();
                                if (photo != null) {
                                    final InputStream inputStream = new ByteArrayInputStream(photo);
                                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_dialog_photo, null);
                                    ((ImageView) view.findViewById(R.id.contact_photo)).setImageBitmap(bitmap);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                            .setNegativeButton(R.string.dismiss, null)
                                            .setView(view)
                                            .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    if (Build.VERSION.SDK_INT > 22) {
                                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                                                                    Manifest.permission.READ_CONTACTS)) {

                                                                new AlertDialog.Builder(context)
                                                                        .setTitle(R.string.permission)
                                                                        .setMessage(R.string.permission_message_write_external_storage)
                                                                        .setPositiveButton(R.string.permission_grant, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                ActivityCompat.requestPermissions(mainActivity,
                                                                                        new String[]{Manifest.permission.READ_CONTACTS},
                                                                                        REQUEST_READ_CONTACTS_CONTACT);
                                                                            }
                                                                        }).create().show();
                                                            } else {
                                                                ActivityCompat.requestPermissions(mainActivity,
                                                                        new String[]{Manifest.permission.READ_CONTACTS},
                                                                        REQUEST_READ_CONTACTS_CONTACT);
                                                            }
                                                        } else share_contact_photo();
                                                    } else share_contact_photo();
                                                }

                                                private void share_contact_photo() {
                                                    mainActivity.setFinish_activity(false);
                                                    File tmp_file = null;
                                                    try {
                                                        tmp_file = new File(Environment.getExternalStorageDirectory(), "tmp.png");
                                                        FileOutputStream outputStream = new FileOutputStream(tmp_file);
                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                                        outputStream.flush();
                                                        outputStream.close();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_SHORT).show();
                                                    }
                                                    Intent sendIntent = new Intent();
                                                    sendIntent.setAction(Intent.ACTION_SEND);
                                                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmp_file));
                                                    sendIntent.setType("image/png");
                                                    mainActivity.startActivityForResult(Intent.createChooser(sendIntent, context.getText(R.string.share)), DO_NOT_FINISH_REQUEST_CODE);
                                                }
                                            });
                                    builder.create().show();
                                }
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.contact_info:
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(all_contact.get(getAdapterPosition()).getName());
                        Cursor info_cursor = resolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{String.valueOf(all_contact.get(getAdapterPosition()).getRaw_id())},
                                null);
                        if (info_cursor != null) {
                            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.contact_about, null);
                            int[] d = new int[]{R.id.number0, R.id.number1, R.id.number2, R.id.number3, R.id.number4, R.id.number5, R.id.number6};
                            for (int i = 0; info_cursor.moveToNext() && i < 6; i++) {
                                view.findViewById(d[i]).setVisibility(View.VISIBLE);
                                ((TextView) view.findViewById(d[i])).setText(info_cursor.getString(info_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            }
                            if ((view.findViewById(d[0])).getVisibility() == View.VISIBLE)
                                builder.setView(view);
                            else builder.setMessage(R.string.no_data_found);
                        } else
                            builder.setMessage(R.string.no_data_found);
                        builder.setNegativeButton(R.string.dismiss, null);
                        builder.create().show();

                        if (info_cursor != null && !info_cursor.isClosed())
                            info_cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private class Load_Contact_Photo extends AsyncTask<String, Void, Bitmap> {

        String contact_name;
        private final WeakReference<ImageView> imageViewReference;

        Load_Contact_Photo(ImageView imageView, String contact_name) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.contact_name = contact_name;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                final Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DATA15},
                        ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " =?",
                        new String[]{strings[0], android.provider.ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}
                        , null);

                InputStream inputStream = null;
                if (cursor.moveToFirst()) {
                    byte[] photo = cursor.getBlob(0);
                    if (photo != null)
                        inputStream = new ByteArrayInputStream(photo);
                    cursor.close();
                }
                if (inputStream != null)
                    return BitmapFactory.decodeStream(inputStream);
                else {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.contact_developer, Toast.LENGTH_LONG).show();
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