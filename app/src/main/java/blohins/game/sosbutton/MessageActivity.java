package blohins.game.sosbutton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

public class MessageActivity extends AppCompatActivity {
    final static  String TAG = "LOG";
    final static String EMPTY = "Please, enter all the fields";
    private static final int REQUEST_SMS_SEND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messsage_activity);


        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "PERMISSION NOT GRANTED");
            ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS_SEND);
        }
        else
        {
            Log.i(TAG, "PERMISSION GRANTED");
            getStarted();
        }

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_back");
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void getStarted(){
        TextView message_tv = findViewById(R.id.message_et);
        EditText phone_et = findViewById(R.id.phone_et);
        EditText time_et = findViewById(R.id.time_et);
        Button btn_send = findViewById(R.id.btn_send);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setEnabled(false);
        btn_cancel.setVisibility(View.INVISIBLE);
        Button btn_back = findViewById(R.id.btn_back);



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btn_send");
                String phone = phone_et.getText().toString();
                String time = time_et.getText().toString();
                String message = message_tv.getText().toString();
                if (phone_et.getText().toString().equals("") || time_et.getText().toString().equals("")) {
                    Log.i(TAG, "empty EditText");
                    Toast.makeText(MessageActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "phone = " + phone + "; time = " + time +", message: ");
                    timer(phone, time, message);
                }
            }
        });

    }

    public void timer(String phone, String time, String message) {
        new CountDownTimer(TimeUnit.MINUTES.toMillis(Integer.parseInt(time)), 1000) {

            @Override
            public void onTick(long l) {

                Button btn_call = findViewById(R.id.btn_send);
                btn_call.setEnabled(false);
                btn_call.setVisibility(View.INVISIBLE);

                Button btn_cancel = findViewById(R.id.btn_cancel);
                btn_cancel.setEnabled(true);
                btn_cancel.setVisibility(View.VISIBLE);

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
                        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "sending...");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, null, null);
            }
        }.start();
    }

}
