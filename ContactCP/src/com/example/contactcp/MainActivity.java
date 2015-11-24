package com.example.contactcp;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		readCallLog();
	}

	/**
	 * 读取通话记录
	 */
	private void readCallLog() {
		ContentResolver cr = this.getContentResolver();
		Uri uriCallLog = Calls.CONTENT_URI;
		String[] projection = new String[] { Calls._ID, Calls.NUMBER,
				Calls.DURATION, Calls.TYPE };
		Cursor c = cr.query(uriCallLog, projection, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				Log.i("info", "ID: " + c.getString(0));
				Log.i("info", "NUMBER: " + c.getString(1));
				Log.i("info", "DURATION: " + c.getString(2));
				Log.i("info", "TYPE: " + c.getString(3));
			}
		}

	}

	/**
	 * 读取联系人
	 */
	private void readContact() {
		ContentResolver cr = this.getContentResolver();
		ContentValues values = new ContentValues();
		Uri uriContact = Contacts.CONTENT_URI;
		Uri uriData = Data.CONTENT_URI;

		String[] projection = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME };
		Cursor c = cr.query(uriContact, projection, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				int id = c.getInt(0);
				String name = c.getString(1);
				Log.i("info", "ID: " + id);
				Log.i("info", "Name: " + name);
				Cursor c1 = cr.query(Phone.CONTENT_URI,
						new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "="
								+ id, null, null);
				if (c1 != null) {
					while (c1.moveToNext()) {
						String number = c1.getString(0);
						Log.i("info", "Phone: " + number);
					}
				}
				Uri uri = ContentUris.withAppendedId(uriContact, id);
				InputStream is = Contacts.openContactPhotoInputStream(cr, uri);
				Bitmap bm = BitmapFactory.decodeStream(is);
				Log.i("info", "头像: " + bm);
			}
		}

	}

	/**
	 * 添加联系人
	 */
	private void insertContact() {
		ContentResolver cr = this.getContentResolver();
		ContentValues values = new ContentValues();
		Uri uriRawContact = RawContacts.CONTENT_URI;
		Uri uriContactData = Data.CONTENT_URI;

		Uri rawUri = cr.insert(uriRawContact, values);
		long rawContactId = ContentUris.parseId(rawUri);

		values.clear();
		values.put(StructuredName.RAW_CONTACT_ID, rawContactId);
		values.put(StructuredName.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.DISPLAY_NAME, "111AAA");
		cr.insert(uriContactData, values);

		values.clear();
		values.put(Phone.RAW_CONTACT_ID, rawContactId);
		values.put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_HOME);
		values.put(Phone.NUMBER, "13540154458");
		cr.insert(uriContactData, values);

		values.clear();
		values.put(Email.RAW_CONTACT_ID, rawContactId);
		values.put(Email.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.TYPE, Email.TYPE_HOME);
		values.put(Email.DATA, "laogan2011@gmail.com");
		cr.insert(uriContactData, values);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
