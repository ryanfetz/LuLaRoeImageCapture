package com.lularoe.erinfetz.core.ui;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarManager {
    private final View view;
    private final Resources resources;

    public SnackbarManager(View view, Resources resources){
        this.view = view;
        this.resources = resources;
    }

    public void show(int resId){
        show(resId, Snackbar.LENGTH_SHORT);
    }

    public void show(int resId, int length){
        Snackbar.make(view, resources.getString(resId), length).show();
    }
    public void shortShow(int resId){
        show(resId, Snackbar.LENGTH_SHORT);
    }
    public void longShow(int resId){
        show(resId, Snackbar.LENGTH_LONG);
    }
    public void indefiniteShow(int resId){
        show(resId, Snackbar.LENGTH_INDEFINITE);
    }

    public void show(String message){
        show(message, Snackbar.LENGTH_SHORT);
    }
    public void show(String message, int length){
        Snackbar.make(view, message, length).show();
    }

    public void shortShow(String message){
        show(message, Snackbar.LENGTH_SHORT);
    }
    public void longShow(String message){
        show(message, Snackbar.LENGTH_LONG);
    }
    public void indefiniteShow(String message){
        show(message, Snackbar.LENGTH_INDEFINITE);
    }
}
