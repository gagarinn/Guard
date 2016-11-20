package net.burmaka.guard.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.burmaka.guard.R;
import net.burmaka.guard.model.Constants;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";


    Button btnFromContacts;
    EditText etPhoneNumber;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(Constants.GUARD_SETTINS_PREFS , MODE_PRIVATE);

        btnFromContacts = (Button)findViewById(R.id.btn_from_contacts);
        btnFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromeContacts();
            }
        });


        etPhoneNumber = (EditText)findViewById(R.id.et_phone_number);
        String phoneNumber = prefs.getString(Constants.SETTINS_PHONE_NUMBER, "");
        if (phoneNumber != null && !phoneNumber.isEmpty()){
            etPhoneNumber.setText(phoneNumber);
        }

    }

    private void savePrefs() {
        if (prefs == null){
            prefs = getSharedPreferences(Constants.GUARD_SETTINS_PREFS , MODE_PRIVATE);
        }
        SharedPreferences.Editor ed = prefs.edit();

        if (etPhoneNumber != null) {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (phoneNumber != null && !phoneNumber.isEmpty())
                ed.putString(Constants.SETTINS_PHONE_NUMBER, phoneNumber);
        }
        ed.commit();
    }

    private void chooseFromeContacts() {
        Log.d(TAG, "------SettingsActivity : chooseFromeContacts: ");
        Toast.makeText(SettingsActivity.this, "TODO chooseFromeContacts", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePrefs();
    }

}
