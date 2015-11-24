package com.example.contactcp;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * 读取手机联系人数据
 */
public class ReadContactTask extends AsyncTask<Void, Void, ArrayList<Contact>> {
	private static final String TAG = "ReadContactTask";
	public static final int FLAG_READ_CONTACT = 1;
	private Context mContext;
	private Handler mHandler;

	public ReadContactTask(Context mContext, Handler mHandler) {
		if (mHandler == null)
			throw new IllegalArgumentException("Argument Handler is null...");
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	protected ArrayList<Contact> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return readContactFromPhone();
	}

	@Override
	protected void onPostExecute(ArrayList<Contact> result) {
		// TODO Auto-generated method stub
		Message msg = mHandler.obtainMessage(FLAG_READ_CONTACT, result);
		msg.sendToTarget();
	}

	/**
	 * 从手机读取联系人数据
	 */
	public ArrayList<Contact> readContactFromPhone() {
		ArrayList<Contact> contacts = null;
		ContentResolver cr = mContext.getContentResolver();
		Uri uriContact = Contacts.CONTENT_URI;
		String[] projection = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME };
		Cursor c = cr.query(uriContact, projection, null, null, null);
		if (c != null) {
			contacts = new ArrayList<Contact>();
			Contact contact = null;
			while (c.moveToNext()) {
				// 获取ID和Name
				contact = new Contact();
				int id = c.getInt(0);
				String name = c.getString(1);
				contact.setName(name);
				Cursor c1 = cr.query(Phone.CONTENT_URI,
						new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "="
								+ id, null, null);
				if (c1 != null) {
					while (c1.moveToNext()) {
						// 获取号码
						String number = c1.getString(0);
						contact.setPhone(number);
						break;
					}
				}
				// 获取头像Uri地址
				Uri uriHeadIcon = ContentUris.withAppendedId(uriContact, id);
				contact.setHeadIconUri(uriHeadIcon);
				// 添加到集合
				contacts.add(contact);
			}
		}

		return contacts;
	}

}
