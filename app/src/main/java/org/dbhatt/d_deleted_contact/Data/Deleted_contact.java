package org.dbhatt.d_deleted_contact.Data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dbhatt.d_deleted_contact.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devsb on 18-09-2016.
 */
public class Deleted_contact extends RecyclerView.Adapter<Deleted_contact.Contact> {

    private Context context;
    private boolean rtl = false;
    private Random rnd;
    private Paint paint;
    private org.dbhatt.d_deleted_contact.Data.Contact contact;
    private ContentResolver resolver;
    private ArrayList<org.dbhatt.d_deleted_contact.Data.Contact> deleted_contact;

    public Deleted_contact(ArrayList<org.dbhatt.d_deleted_contact.Data.Contact> deleted_contact, Context context) {
        this.deleted_contact = deleted_contact;
        resolver = context.getContentResolver();
        rnd = new Random();
        paint = new Paint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            rtl = context.getResources().getConfiguration().getLayoutDirection() != View.LAYOUT_DIRECTION_LTR;
        this.context = context;
    }

    @Override
    public Deleted_contact.Contact onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);
        return new Deleted_contact.Contact(view);
    }

    @Override
    public void onBindViewHolder(Deleted_contact.Contact holder, int position) {
        contact = deleted_contact.get(position);
        holder.contact_name.setText(contact.getName());
        holder.account_type.setText(contact.getAccount_type());
        new Load_Contact_Photo(holder.contact_photo, contact.getName()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(contact.getId()));
    }

    @Override
    public int getItemCount() {
        return deleted_contact.size();
    }

    public class Contact extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView contact_raw;
        TextView contact_name, account_type;
        ImageView contact_photo, contact_info;

        public Contact(View itemView) {
            super(itemView);

            contact_raw = (CardView) itemView.findViewById(R.id.contact_raw);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            account_type = (TextView) itemView.findViewById(R.id.contact_account_type);
            contact_photo = (ImageView) itemView.findViewById(R.id.contact_photo);
            contact_info = (ImageView) itemView.findViewById(R.id.contact_info);

            contact_raw.setOnClickListener(this);
            contact_info.setOnClickListener(this);
            contact_photo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.contact_raw:
                    break;
                case R.id.contact_photo:
                    final Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DATA15},
                            ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " =?",
                            new String[]{String.valueOf(deleted_contact.get(getAdapterPosition()).getId()), android.provider.ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}
                            , null);
                    if (cursor.moveToFirst()) {
                        byte[] photo = cursor.getBlob(0);
                        cursor.close();
                        if (photo != null) {
                            InputStream inputStream = new ByteArrayInputStream(photo);
                            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_dialog_photo, null);
                            ((ImageView) view.findViewById(R.id.contact_photo)).setImageBitmap(BitmapFactory.decodeStream(inputStream));
                            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                    .setNegativeButton(R.string.dismiss, null)
                                    .setView(view)
                                    .setPositiveButton(R.string.share, null);
                            builder.create().show();
                        }
                    }
                    break;
                case R.id.contact_info:
                    break;
            }
        }
    }

    class Load_Contact_Photo extends AsyncTask<String, Void, Bitmap> {

        String contact_name;
        private final WeakReference<ImageView> imageViewReference;

        Load_Contact_Photo(ImageView imageView, String contact_name) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.contact_name = contact_name;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

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
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}