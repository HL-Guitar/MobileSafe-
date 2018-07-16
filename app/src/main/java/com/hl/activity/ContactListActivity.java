package com.hl.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hl.mobilesafe.R;
import com.hl.utils.PermissionUtils;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by UTT on 2018/7/8.
 */

public class ContactListActivity extends Activity {
    private ListView lv_contact;
    private List<TreeMap<String,String>> contactList = new ArrayList<TreeMap<String,String>>();
    MyAdapter mMyAdapter;
    private ProgressBar pro_contact;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMyAdapter = new MyAdapter();
            lv_contact.setAdapter(mMyAdapter);
            pro_contact.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        PermissionUtils.requestMorePermissions(this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},2);

    }

    /**
     * 获取联系人数据
     */
    private void initData() {
        //因为获取联系人是一个耗时的操作，所以放到子线程里面、
        new Thread(new Runnable() {
            public void run() {
                try{
                    contactList.clear();
                    TreeMap<String,String> treeMap = new TreeMap<String,String>(new CollatorComparator());
                    ContentResolver contentResolver = getContentResolver();
                    // 获得所有的联系人
                    Cursor cursor = contentResolver.query(
                            ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    // 循环遍历
                    if (cursor.moveToFirst()) {

                        int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);

                        int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        do {
                            String contactId = cursor.getString(idColumn);
                            // 获得联系人姓名
                            String disPlayName = cursor.getString(displayNameColumn);
                            // 查看该联系人有多少个电话号码。如果没有这返回值为0
                            int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            //在联系人数量不为空的情况下执行
                            if (phoneCount > 0) {
                            // 获得联系人的电话号码列表
                                Cursor phonesCursor = getContentResolver()
                                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = " + contactId, null,
                                                null);
                                if (phonesCursor.moveToFirst()) {

                                    do {
                                      // 遍历所有的电话号码
                                        String phoneNumber = phonesCursor.getString(phonesCursor
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                        treeMap.put(disPlayName,phoneNumber);
                                    } while (phonesCursor.moveToNext());
                                }
                                phonesCursor.close();
                            }

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    Set<String> get = treeMap.keySet();
                    for (String key:get) {
                        TreeMap<String,String> treeMap1 = new TreeMap<String,String>();
                        treeMap1.put("name",key);
                        treeMap1.put("phone",treeMap.get(key));
                        contactList.add(treeMap1);
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    //发送一个空消息通知填充完成
                    mhandler.sendEmptyMessage(0);
                }
            }
        ).start();
        }
    public class CollatorComparator implements Comparator {

        Collator collator = Collator.getInstance();

        public int compare(Object element1, Object element2) {

            CollationKey key1 = collator.getCollationKey(element1.toString());

            CollationKey key2 = collator.getCollationKey(element2.toString());

            return key1.compareTo(key2);

        }
    }

        class MyAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return contactList.size();
            }

            @Override
            public TreeMap<String, String> getItem(int position) {
                return contactList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View listview_contact_item = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
                TextView tv_name = (TextView) listview_contact_item.findViewById(R.id.tv_name);
                TextView tv_phone = (TextView) listview_contact_item.findViewById(R.id.tv_phone);

                tv_name.setText(contactList.get(position).get("name"));
                tv_phone.setText(contactList.get(position).get("phone"));

                return listview_contact_item;
            }
        }
        /**
         * 初始化界面控件
         */
    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        pro_contact = (ProgressBar) findViewById(R.id.pro_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1,获取点中条目的索引指向集合中的对象
                if (mMyAdapter!=null){
                    TreeMap<String,String> treeMap = mMyAdapter.getItem(position);
                    //2.获取当前条目的电话号码
                    String phone = treeMap.get("phone");
                    //3.吃电话号码要给第三个界面使用

                    //4.在结束之前需要将数据返回回去
                    Intent it = new Intent();
                    it.putExtra("phone",phone);
                    setResult(0,it);
                    finish();
                }
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==2){
            initData();
        }
    }
}
