package com.alsash.reciper.ui.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.alsash.reciper.R;

public class MenuHelper {

    public static void tintItems(Context context, Menu menu, int mainMenuColor, int subMenuColor) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                icon.mutate();
                DrawableCompat.setTint(icon, ContextCompat.getColor(context, mainMenuColor));
            }
            if (item.hasSubMenu()) {
                tintItems(context, item.getSubMenu(), subMenuColor, subMenuColor);
            }
        }
    }

    public static void tintItems(Context context, Menu menu) {
        tintItems(context, menu, R.color.white, R.color.black_a054);
    }
}
