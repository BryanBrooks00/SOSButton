package blohins.game.sosbutton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CallActivity extends AppCompatActivity {

    final static String TAG = "LOG";
    final static String EMPTY = "Please, enter all the fields";
    EditText phone_et;
    EditText time_et;
    Button btn_call;
    Button btn_cancel;
    Button btn_back;
    TextView time_left_tv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        context = this;
        phone_et = findViewById(R.id.phone_et);
        time_et = findViewById(R.id.time_et);
        btn_call = findViewById(R.id.btn_call);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_back = findViewById(R.id.btn_back);
        time_left_tv = findViewById(R.id.time_left_tv);

        checkState();
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_back");
                Intent intent = new Intent(CallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_call");
                String phone = phone_et.getText().toString();
                String time = time_et.getText().toString();
                if (phone_et.getText().toString().equals("") || time_et.getText().toString().equals("")) {
                    Log.i(TAG, "empty EditText");
                    Toast.makeText(CallActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "phone = " + phone + "; time = " + time);
                    Preferences.setAction(context, "call");
                    Preferences.setStateIndex(context, "1");
                    Preferences.setLastTime(context, time);
                    Preferences.setLastNum(context, phone);
                    checkState();
                    context.startService(new Intent(getApplicationContext(), BackgroundService.class));
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_cancel");
                Preferences.setStateIndex(context, "0");
                checkState();
                stopService(new Intent(CallActivity.this, BackgroundService.class));
                recreate();
            }
        });
    }

    public void callPhone() {
        Log.i(TAG, "calling...");
        long phone = Long.parseLong(Preferences.getLastNum(MainActivity.getContext()));
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    public void checkState() {
        if (Preferences.getStateIndex(context) != null) {
            Log.i(TAG, "stateIndex = " + Preferences.getStateIndex(context));
            if (Preferences.getStateIndex(context).equals("1")) {
                btn_cancel.setEnabled(true);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_call.setEnabled(false);
                btn_call.setVisibility(View.INVISIBLE);
                time_left_tv.setVisibility(View.VISIBLE);
            } else {
                btn_cancel.setEnabled(false);
                btn_cancel.setVisibility(View.INVISIBLE);
                btn_call.setEnabled(true);
                btn_call.setVisibility(View.VISIBLE);
                time_left_tv.setVisibility(View.INVISIBLE);
                phone_et.setText("");
                time_et.setText("");
            }
            ;
        } else {
            Log.i(TAG, "ERROR: stateIndex = " + Preferences.getStateIndex(context));
        }
    }

    public void timer(long seconds) {
        if (seconds <=0){
            time_left_tv.setText(R.string.dialing);
        } else {
            time_left_tv.setText(String.valueOf(seconds));
        }
        Log.i(TAG, "seconds = " + seconds);
    }
}
