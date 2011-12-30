package com.itjiaozi.iris.adapter;

import com.itjiaozi.iris.db.TbContactCache;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class ContactListAdapter extends CursorAdapter implements Filterable {

    public static final int VIEW_TYPE_CONTACT_NAME = 0xFF0123F1;
    public static final int VIEW_TYPE_CONTACT_NUMBER = 0xFF0123F2;

    public ContactListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

        TbContactCache tbc = TbContactCache.parseCursor(cursor);
        String text = String.format("%s\t\t%s", tbc.FullName, tbc.Number);
        view.setTag(VIEW_TYPE_CONTACT_NAME, tbc.FullName);
        view.setTag(VIEW_TYPE_CONTACT_NUMBER, tbc.Number);
        view.setText(text);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TbContactCache tbc = TbContactCache.parseCursor(cursor);
        String text = String.format("%s\t\t%s", tbc.FullName, tbc.Number);
        view.setTag(VIEW_TYPE_CONTACT_NAME, tbc.FullName);
        view.setTag(VIEW_TYPE_CONTACT_NUMBER, tbc.Number);
        ((TextView) view).setText(text);
    }

    @Override
    public String convertToString(Cursor cursor) {
        TbContactCache tbc = TbContactCache.parseCursor(cursor);
        String text = String.format("%s\t\t%s", tbc.FullName, tbc.Number);
        return text;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return TbContactCache.queryContactsCursor(constraint + "");
    }
}