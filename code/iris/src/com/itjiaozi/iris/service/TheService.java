package com.itjiaozi.iris.service;

import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.util.SPUtil;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

public class TheService extends Service {

    private static final String TAG = TheService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactsObserver);
        getContentResolver().registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, true, mContactsObserver);

    }

    private static ContentObserver mContactsObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            long lastTime = SPUtil.getLong(getClass().getName() + "_last_sync", 0);
            if (System.currentTimeMillis() - lastTime > 1000 * 60 * 60) {
                Log.d(TAG, "contacts changed");
                TheContacts.syncContacts(true);
                SPUtil.put(getClass().getName() + "_last_sync", System.currentTimeMillis());
            }
        }
    };

    public void onDestroy() {
        Log.d(TAG, "IrisService onDestroy()");
        getContentResolver().unregisterContentObserver(mContactsObserver);
    };
}
