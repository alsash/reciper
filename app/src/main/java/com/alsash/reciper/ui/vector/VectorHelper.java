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
     * @param textView              target view
     * @param tintColor             tint color
     * @param leftRightTopBottomRes Left Right Top and Bottom vector drawables
     */
    public void setCompoundDrawables(TextView textView, @Nullable Integer tintColor,
                                     Integer... leftRightTopBottomRes) {
        if (textView == null) return;

        Drawable[] icons = new Drawable[4];
        for (int i = 0; i < icons.length; i++) {
            if (leftRightTopBottomRes.length > i && leftRightTopBottomRes[i] != null) {
                Drawable icon = create(leftRightTopBottomRes[i]);
                tint(icon, tintColor);
                icons[i] = icon;
            } else {
                icons[i] = null;
            }
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(icons[0], icons[1], icons[2], icons[3]);
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
