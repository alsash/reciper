package com.alsash.reciper.ui.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alsash.reciper.R;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableLong;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.app.lib.TimeLong;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * A photo dialog helper
 */
public class SimpleDialog {

    public static void showEditPhotoUrl(Context context,
                                        @Nullable Photo photo,
                                        @NonNull final MutableString listener) {

        @SuppressLint("InflateParams")
        final EditText editText = (EditText) LayoutInflater.from(context)
                .inflate(R.layout.view_dialog_photo_url, null);

        final String url = (photo != null) ? photo.getUrl() : null;
        if (url != null) editText.setText(url);

        new AlertDialog.Builder(context)
                .setTitle(R.string.simple_dialog_title_photo_url)
                .setView(editText)
                .setPositiveButton(context.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newUrl = editText.getText().toString().trim();
                                if (!newUrl.startsWith("https://")
                                        && !newUrl.startsWith("http://")) {
                                    if (newUrl.length() > 0) newUrl = "https://" + newUrl;
                                }
                                if (!newUrl.equals(url)) listener.set(newUrl.trim());
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(context.getString(android.R.string.cancel), null)
                .show();
    }

    public static void showEditRecipeName(Context context,
                                          @Nullable Recipe recipe,
                                          @NonNull final MutableString listener) {

        @SuppressLint("InflateParams")
        final EditText editText = (EditText) LayoutInflater.from(context)
                .inflate(R.layout.view_dialog_recipe_name, null);

        final String name = (recipe != null) ? recipe.getName() : null;
        if (name != null) editText.setText(name);

        new AlertDialog.Builder(context)
                .setTitle(R.string.simple_dialog_title_recipe_name)
                .setView(editText)
                .setPositiveButton(context.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newName = editText.getText().toString().trim();
                                if (!newName.equals(name)) listener.set(newName);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(context.getString(android.R.string.cancel), null)
                .show();
    }

    public static void showEditTime(Context context, final MutableLong listener) {
        final long milliseconds = listener.get();
        int[] hm = TimeLong.getHoursAndMinutes(milliseconds);
        TimePickerDialog dialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        long millis = TimeLong.getTimeFromHoursAndMinutes(hourOfDay, minute);
                        if (millis != milliseconds) listener.set(millis);
                    }
                },
                hm[0], hm[1], true);
        dialog.show();
    }

    public static void showConfirmDelete(Context context,
                                         String entityName,
                                         final MutableBoolean listener) {

        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.simple_dialog_message_confirm_delete, entityName))
                .setPositiveButton(context.getString(android.R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.set(true);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(context.getString(android.R.string.no), null)
                .show();
    }
}
