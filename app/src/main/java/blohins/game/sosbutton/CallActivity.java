package blohins.game.sosbutton;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.sax.SAXResult;

public class CallActivity extends AppCompatActivity {

    final static String TAG = "LOG";
    final static String EMPTY = "Please, enter all the fields";
    private static final int REQUEST_PHONE_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "PERMISSION NOT GRANTED");
            ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            Log.i(TAG, "PERMISSION_GRANTED");
            getStarted();
        }

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_back");
                Intent intent = new Intent(CallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void getStarted() {
        EditText phone_et = findViewById(R.id.phone_et);
        EditText time_et = findViewById(R.id.time_et);
        Button btn_call = findViewById(R.id.btn_call);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setEnabled(false);
        btn_cancel.setVisibility(View.INVISIBLE);


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
                    timer(phone, time);
                }
            }
        });


    }

    public void timer(String phone, String time) {

        Button btn_call = findViewById(R.id.btn_call);
        btn_call.setEnabled(false);
        btn_call.setVisibility(View.INVISIBLE);

        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setEnabled(true);
        btn_cancel.setVisibility(View.VISIBLE);


        new CountDownTimer(TimeUnit.MINUTES.toMillis(Integer.parseInt(time)), 1000) {

            @Override
            public void onTick(long l) {
                TextView time_left_tv = findViewById(R.id.time_left_tv);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(l);
                time_left_tv.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                Log.i(TAG, "seconds = " + seconds);

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (seconds == 58) {
                    Log.i(TAG, "vibrate");
                    vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE));
                }

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "btn_cancel");
                        cancel();
                        recreate();
                    }
                });

                Button btn_back = findViewById(R.id.btn_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "btn_back");
                        cancel();
                        Intent intent = new Intent(CallActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "calling...");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        }.start();


    }

}