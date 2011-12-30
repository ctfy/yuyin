package com.itjiaozi.iris.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.itjiaozi.iris.db.TbAppCache;

public class AppListAdapter extends CursorAdapter implements Filterable {

    public static final int VIEW_TYPE_APP_NAME = 0xFF00FCFA;
    public static final int VIEW_TYPE_APP_PACKAGE = 0xFF00FCFB;

    public AppListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

        TbAppCache tac = TbAppCache.parseCursor(cursor);
        String text = String.format("%s", tac.Name);
        view.setTag(VIEW_TYPE_APP_NAME, tac.Name);
        view.setTag(VIEW_TYPE_APP_PACKAGE, tac.PackageName);
        view.setText(text);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TbAppCache tac = TbAppCache.parseCursor(cursor);
        String text = String.format("%s", tac.Name);
        view.setTag(VIEW_TYPE_APP_NAME, tac.Name);
        view.setTag(VIEW_TYPE_APP_PACKAGE, tac.PackageName);
        ((TextView) view).setText(text);
    }

    @Override
    public String convertToString(Cursor cursor) {
        TbAppCache tac = TbAppCache.parseCursor(cursor);
        String text = String.format("%s", tac.Name);
        return text;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return TbAppCache.queryLikeAppCursorByName(constraint + "");
    }
}