package blohins.game.sosbutton;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService.class";
    Context context;
    CallActivity ca;

    protected void startJob() {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startJob();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ca = new CallActivity();
        context = getApplicationContext();
        String CHANNEL_ID = "my_channel_01";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(Preferences.getSecondsLeft(getApplicationContext())
                            + " to "
                            + Preferences.getAction(getApplicationContext())
                            + " " + Preferences.getLastNum(getApplicationContext())).build();

            startForeground(1, notification);

            Log.i(TAG, "Service was started");
            String num = Preferences.getLastNum(getApplicationContext());
            if (num == null) {
                Log.i(TAG, "NULL Preferences = " + num);
            } else {
                        long time = Long.parseLong(Preferences.getLastTime(getApplicationContext()));
                        new CountDownTimer(TimeUnit.MINUTES.toMillis(time), 1000) {

                            @Override
                            public void onTick(long l) {
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(l);
                                Preferences.setSecondsLeft(getApplicationContext(), String.valueOf(seconds));
                                Log.i(TAG, "seconds = " + seconds);
                               // ca.timer(seconds);
                            }

                            @Override
                            public void onFinish() {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                long phone = Long.parseLong(Preferences.getLastNum(getApplicationContext()));
                                String message = Preferences.getLastMessage(getApplicationContext());
                                String action = Preferences.getAction(getApplicationContext());
                                Log.i(TAG, action);
                                if(action != null) {
                                    if (action.equals("call")) {
                                        Log.i(TAG, "calling...");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                                        intent.setData(Uri.parse("tel:" + phone));
                                        context.startActivity(intent);
                                    } else if (action.equals("sms")){
                                        Log.i(TAG, "sending...");
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(String.valueOf(phone), null, message, null, null);
                                    }
                                } else{
                                    Log.i(TAG, "ERROR: action = null");
                                }
                                Preferences.clearPreferences(getApplicationContext(), "0");
                            }
                        });
                            }
                        }.start();
                Log.i(TAG, "Preferences = " + num);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service was stop");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
