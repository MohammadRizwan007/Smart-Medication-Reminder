package com.example.smartmedicationreminder.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.smartmedicationreminder.HomeFragment;
import com.example.smartmedicationreminder.ProfilePatientActivity;
import com.example.smartmedicationreminder.R;

public class MyAlertDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alert_dialog);

//          requestWindowFeature(Window.FEATURE_NO_TITLE); //hide activity title
            showDialogAlert(MyAlertDialogActivity.this,"Patient Medicines Reminder","Did you take medicine?");
    }


    public void showDialogAlert(final Context context, String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MyAlertDialogActivity.this, "Good !! Hope You'll Always Keep Medicine On Time", Toast.LENGTH_LONG).show();
//                        sendSMS("03323256872",context,"X Patients have taken medicines.");
                                dialog.cancel();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MyAlertDialogActivity.this, "Allas !! We Are Going To Tell The Attendant", Toast.LENGTH_LONG).show();
//                        sendSMS("03412030258",context,"X Patient not take the medicines");
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                Intent i=new Intent(MyApplication.getAppContext(), ProfilePatientActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);

            }
        }, 10000);

    }




//    private void sendSMS(String number, Context context, String sms) {
//        try {
//            SmsManager.getDefault().sendTextMessage(number, null, sms, null, null);
//        } catch (IndexOutOfBoundsException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (ActivityNotFoundException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (SecurityException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (IllegalStateException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (NullPointerException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (OutOfMemoryError e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (RuntimeException e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } catch (Exception e) {
//            Log.e("ExceptionError", " = " + e.getMessage());
//        } finally {
//        }
//    }
}