package com.alsash.reciper.ui.vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alsash.reciper.R;

import java.util.ArrayList;

public class VectorHelper {

    private final Context context;
    private final Resources.Theme theme;

    public VectorHelper(Context context) {
        this(context, context.getTheme());
    }

    public VectorHelper(Context context, Resources.Theme theme) {
        this.context = context;
        this.theme = theme;
    }

    public void tintMenuItems(Menu menu) {
        tintMenuItems(menu, R.color.white, R.color.black_a054);
    }

    public void tintMenuItems(Menu menu, int mainMenuColor, int subMenuColor) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                icon.mutate();
                DrawableCompat.setTint(icon, ContextCompat.getColor(context, mainMenuColor));
            }
            if (item.hasSubMenu()) {
                tintMenuItems(item.getSubMenu(), subMenuColor, subMenuColor);
            }
        }
    }

    /**
     * Set vector drawable into textView or other textView inheritor
     *
     * @param textView  target view
     * @param lrtbRes   Left Right Top and Bottom vector drawables
     * @param tintColor tint color
     */
    public void setCompoundDrawables(TextView textView, Integer[] lrtbRes,
                                     @Nullable Integer tintColor) {
        if (textView == null) return;

        ArrayList<Drawable> icons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (lrtbRes.length > i && lrtbRes[i] != null) {
                Drawable icon = tint(create(lrtbRes[0]), tintColor);
                icons.add(icon);
            } else {
                icons.add(null);
            }
        }

        textView.setCompoundDrawablesWithIntrinsicBounds(
                icons.get(0),
                icons.get(1),
                icons.get(2),
                icons.get(3));
    }

    @Nullable
    private Drawable create(@DrawableRes int resId) {
        return VectorDrawableCompat.create(context.getResources(), resId, theme);
    }

    private Drawable tint(@Nullable Drawable icon, @Nullable Integer color) {
        if (icon == null || color == null) return icon;
        icon.mutate();
        DrawableCompat.setTint(icon, ContextCompat.getColor(context, color));
        return icon;
    }
}
