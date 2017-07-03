package com.alsash.reciper.ui.view.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.mvp.model.entity.Photo;

/**
 * A photo dialog helper
 */
public class PhotoDialogHelper {

    public static void show(Context context,
                            @Nullable Photo photo,
                            @NonNull final MutableString listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        final String url = (photo != null) ? photo.getUrl() : null;
        if (url != null) editText.setText(url);

        builder.setView(editText);
        builder.setPositiveButton(context.getString(android.R.string.copyUrl),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUrl = editText.getText().toString();
                        if (!newUrl.equals(url)) listener.set(newUrl);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(context.getString(android.R.string.cancel), null);
        builder.show();
    }
}
