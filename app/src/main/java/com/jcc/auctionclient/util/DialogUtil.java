package com.jcc.auctionclient.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.jcc.auctionclient.AuctionClientActivity;

/**
 * Created by juyuan on 1/8/2016.
 */
public class DialogUtil {
    public static void showDialog(final Context ctx, String msg, boolean goHome){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
        if (goHome){
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ctx, AuctionClientActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
            });
        } else {
            builder.setPositiveButton("确定", null);
        }
        builder.create().show();
    }

    public static void showDialog(Context ctx, View view){
        new AlertDialog.Builder(ctx).setView(view).setCancelable(false)
                .setPositiveButton("确定", null).create().show();
    }

    public static void showDialog(final Context ctx, String msg, final Class<?> targetActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ctx, targetActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            };
        });
        builder.create().show();
    }
}
