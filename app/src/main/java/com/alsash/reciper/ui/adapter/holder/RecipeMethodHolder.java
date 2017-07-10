package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Method;

/**
 * A recipe method view holder
 */
public class RecipeMethodHolder extends RecyclerView.ViewHolder implements DragAndDropHolder {

    private final TextView number;
    private final ImageButton edit;
    private final EditText body;

    public RecipeMethodHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        number = (TextView) itemView.findViewById(R.id.item_method_number);
        edit = (ImageButton) itemView.findViewById(R.id.item_method_edit);
        body = (EditText) itemView.findViewById(R.id.item_method_body);
        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onMoveAtDragAndDrop() {
        number.setText(String.valueOf(getAdapterPosition() + 1));
    }

    public void bindMethod(Method method) {
        number.setText(String.valueOf(getAdapterPosition() + 1));
        body.setText(method.getBody());
        body.requestLayout();
    }

    public String getBody() {
        return body.getText().toString();
    }

    public void setEditable(boolean editable) {
        edit.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        body.setEnabled(editable);
        body.setEnabled(editable);
        body.setFocusable(editable);
        body.setFocusableInTouchMode(editable);
        body.setClickable(editable);
        body.setLongClickable(editable);
        body.setCursorVisible(editable);
        if (editable) body.requestFocus();
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. editListener - must implement View.OnClickListener
     *                  1. dragListener - must implement View.OnTouchListener
     */
    public void setListeners(Object... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    edit.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
                case 1:
                    number.setOnTouchListener((View.OnTouchListener) listeners[i]);
            }
        }
    }
}
