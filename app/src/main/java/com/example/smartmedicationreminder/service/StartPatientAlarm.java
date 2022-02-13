package com.example.smartmedicationreminder.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


import com.example.smartmedicationreminder.AlarmReceiver;
import com.example.smartmedicationreminder.PatientActivity;
import com.example.smartmedicationreminder.ProfilePatientActivity;
import com.example.smartmedicationreminder.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.Context.VIBRATOR_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

/*broadcast reciever*/
public class StartPatientAlarm extends BroadcastReceiver {
    private NotificationManager alarmNotificationManager;
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "alarm_channel";
    static final String ACTION = "ShowDialog";
    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";
    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;
    SharedPreferences pref1;
    Ringtone r;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"WrongConstant", "ResourceAsColor"})
    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(context, notification);
            r.play();
            Vibrator vibrator = (Vibrator) context
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(3000);
            //Notification
            final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final Intent notifIntent = new Intent(context, PatientActivity.class);
            notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            final PendingIntent pIntent = PendingIntent.getActivity(
                    context, 1, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            createNotificationChannel(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
            builder.setContentTitle("Patient Medicine Time");
            builder.setColor(R.color.bottom_blue);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setContentIntent(pIntent);
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_HIGH);
            manager.notify(1, builder.build());
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    Intent i=new Intent(MyApplication.getAppContext(), MyAlertDialogActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                    r.stop();
                }
            }, 10000);
        } catch (IndexOutOfBoundsException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (IllegalArgumentException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (SecurityException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (IllegalStateException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (NullPointerException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (RuntimeException e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        } catch (Exception e) {
            Log.i("errorBlockerStart", e.getMessage() + " = Start");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void createNotificationChannel(Context ctx) {
        if (SDK_INT < O) return;

        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
        if (mgr == null) return;

        final String name = ctx.getString(R.string.common_google_play_services_notification_channel_name);
        if (mgr.getNotificationChannel(name) == null) {
            final NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 500, 1000, 500, 1000, 500});
            channel.setBypassDnd(true);
            mgr.createNotificationChannel(channel);
        }
    }
}
