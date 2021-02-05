package com.EducationIfo.checkInternet;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.Educationinfo.educationinfo.R;

public abstract class NetworkChangeListner extends BroadcastReceiver {

    @Override
    public void onReceieve(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(contex)) { //Internet is not connected
            AlertDialog.Builder builder = new AlertDialog.Builder(contex);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(layout_dialog);

            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.btnRetry);
            //show dialof
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(flase);

            dialog.getWindow().setGravity(Gravity.CENTER);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceieve(context, Intent);
                }
            });

        }
    }
