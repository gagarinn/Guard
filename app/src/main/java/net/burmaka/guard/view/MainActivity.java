package net.burmaka.guard.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.burmaka.guard.R;
import net.burmaka.guard.model.Constants;
import net.burmaka.guard.model.SMSInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    Button btnStart;
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startServiceAndExit();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopServiceAndExit();
            }
        });

        logButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logButton() {
        Button btnLog = (Button)findViewById(R.id.btn_log_sms);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                logAllsms();
                ListView lvLog = (ListView)findViewById(R.id.lv_sms_log);
                SMSLogAdapter smsLogAdapter = new SMSLogAdapter(logAllsms());
                lvLog.setAdapter(smsLogAdapter);
            }
        });


    }

    private List<SMSInfo> logAllsms() {
        SharedPreferences prefs = getSharedPreferences(Constants.GUARD_SETTINS_PREFS , MODE_PRIVATE);
        String number = prefs.getString(Constants.SETTINS_PHONE_NUMBER, "");
        Log.d(TAG, "------MainActivity : logAllsms: number = " + number);
        List list = new ArrayList<SMSInfo>();
        try {

            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = this.getContentResolver().query(uriSms,
                    new String[] { "_id", "thread_id", "address",
                            "person", "date", "body" }, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String person = c.getString(3);
//                    String date = c.getString(4);
                   long dateInt = c.getLong(4);
                    String body = c.getString(5);

                   String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateInt));
//                    if (message.equals(body) && address.equals(number)) {
//                        this.getContentResolver().delete(
//                                Uri.parse("content://sms/" + id), null, null);
//                    }


                    if (address.equals(number) || address.equals("+38" + number)) {
                        SMSInfo smsInfo = new SMSInfo();
                        smsInfo.setId("" + id);
                        smsInfo.setAddress(address);
                        smsInfo.setDate(date);
                        list.add(smsInfo);
                    }

                    Log.d(TAG, "------ logAllsms: id = " + id + ",  body = " + body + ", date = " + date + ", person = " + person);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "------MainActivity : logAllsms: " + e.getMessage());
        }

        return list;
    }

    private void stopServiceAndExit() {
        Log.d(TAG, "------MainActivity : stopServiceAndExit: ");
        Toast.makeText(MainActivity.this, "do nothing", Toast.LENGTH_SHORT).show();
    }

    private void startServiceAndExit() {
        Log.d(TAG, "------MainActivity : startServiceAndExit: ");
        Toast.makeText(MainActivity.this, "do nothing", Toast.LENGTH_SHORT).show();
    }

    class SMSLogAdapter extends BaseAdapter {
        List<SMSInfo> smsList;

        SMSLogAdapter(List<SMSInfo> list){
        smsList = list;
        }

        class ViewHolder {
            public TextView tvId;
            public TextView tvAddress;
            public TextView tvDate;
        }

        @Override
        public int getCount() {
            if (smsList != null) {
                return smsList.size();
            } else return 0;
        }

        @Override
        public Object getItem(int i) {
            return smsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            SMSInfo sms = smsList.get(i);

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.log_item, viewGroup, false);

                holder = new ViewHolder();

                holder.tvId = (TextView) view.findViewById(R.id.tv_item_id);
                holder.tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
                holder.tvDate = (TextView) view.findViewById(R.id.tv_item_date);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tvId.setText(sms.getId());
            holder.tvAddress.setText(sms.getAddress());
            holder.tvDate.setText(sms.getDate());

            return view;
        }
    }
}
