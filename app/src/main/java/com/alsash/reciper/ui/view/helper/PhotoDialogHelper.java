package com.alsash.reciper.ui.view.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

import com.alsash.reciper.R;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.mvp.model.entity.Photo;

/**
 * A photo dialog helper
 */
public class PhotoDialogHelper {

    public static void show(Context context,
                            @Nullable Photo photo,
                            @NonNull final MutableString listener) {

        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        final String url = (photo != null) ? photo.getUrl() : null;
        if (url != null) editText.setText(url);

        new AlertDialog.Builder(context)
                .setTitle(R.string.photo_dialog_title)
                .setView(editText)
                .setPositiveButton(context.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newUrl = editText.getText().toString();
                                if (!newUrl.startsWith("https://")
                                        && !newUrl.startsWith("http://")) {
                                    newUrl = "https://" + newUrl;
                                }
                                if (!newUrl.equals(url)) listener.set(newUrl);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(context.getString(android.R.string.cancel), null)
                .show();
    }
}
