
package com.example.movies.utils.utilities.search_viewer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.SearchView;

public class SearchViewer extends SearchView {
    private OnBackPressListener onBackPressListener;
    private boolean isKeyboardVisible;

    public SearchViewer(Context context) {
        super(context);
    }

    public SearchViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        KeyboardUtils.addKeyboardToggleListener(getContext(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                SearchViewer.this.isKeyboardVisible = isVisible;
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KeyboardUtils.removeAllKeyboardToggleListeners();
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            if (onBackPressListener != null && isKeyboardVisible) //original (onBackPressListener != null && !isKeyboardVisible)
                onBackPressListener.backPress();
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public OnBackPressListener getOnBackPressListener() {
        return onBackPressListener;
    }

    public void setBackPressListener(OnBackPressListener onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
    }

    public interface OnBackPressListener {
        void backPress();
    }
}