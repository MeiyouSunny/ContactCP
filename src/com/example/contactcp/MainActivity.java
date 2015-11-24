package com.example.contactcp;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvInfo = (TextView) findViewById(R.id.info);
		// readCallLog();
		// readContact();

		ReadContactTask task = new ReadContactTask(this, mHandler);
		task.execute();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == ReadContactTask.FLAG_READ_CONTACT) {
				StringBuffer sb = new StringBuffer();
				ArrayList<Contact> contacts = (ArrayList<Contact>) msg.obj;
				if (contacts != null && !contacts.isEmpty()) {
					for (Contact c : contacts) {
						sb.append(c.toString() + "\n");
					}
				}
				tvInfo.setText(new String(sb));
			}
		};
	};

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
			StringBuffer sb = new StringBuffer();
			while (c.moveToNext()) {
				sb.append("ID: " + c.getString(0) + "\n")
						.append("NUMBER: " + c.getString(1) + "\n")
						.append("DURATION: " + c.getString(2) + "\n")
						.append("TYPE: " + c.getString(3) + "\n\n");
				Log.i("info", "ID: " + c.getString(0));
				Log.i("info", "NUMBER: " + c.getString(1));
				Log.i("info", "DURATION: " + c.getString(2));
				Log.i("info", "TYPE: " + c.getString(3));
			}
			tvInfo.setText(new String(sb));
		}
	}

	/**
	 * 读取联系人
	 */
	private void readContact() {
		ContentResolver cr = this.getContentResolver();
		Uri uriContact = Contacts.CONTENT_URI;

		String[] projection = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME };
		Cursor c = cr.query(uriContact, projection, null, null, null);
		if (c != null) {
			StringBuffer sb = new StringBuffer();
			while (c.moveToNext()) {
				int id = c.getInt(0);
				String name = c.getString(1);
				sb.append("ID: " + id + "\n").append("Name: " + name);
				Log.i("info", "ID: " + id);
				Log.i("info", "Name: " + name);
				Cursor c1 = cr.query(Phone.CONTENT_URI,
						new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "="
								+ id, null, null);
				if (c1 != null) {
					while (c1.moveToNext()) {
						String number = c1.getString(0);
						Log.i("info", "Phone: " + number);
						sb.append("Number: " + number + "\n");
					}
				}
				Uri uri = ContentUris.withAppendedId(uriContact, id);
				InputStream is = Contacts.openContactPhotoInputStream(cr, uri);
				Bitmap bm = BitmapFactory.decodeStream(is);
				Log.i("info", "头像: " + bm);
				sb.append("HeadIcon: " + bm + "\n\n");
			}
			tvInfo.setText(new String(sb));
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

}
