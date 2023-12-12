package com.example.oldbooks.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.dialog.Information;
import com.example.oldbooks.dialog.NoInternet;
import com.example.oldbooks.dialog.Warning;
import com.example.oldbooks.extras.Enums;
import com.example.oldbooks.dialog.Acknowledgement;
import com.example.oldbooks.dialog.Confirmation;

public class DialogManager extends Manager {

    //region Singleton
    private static DialogManager instance;
    private DialogManager() {
        Initialize();
    }
    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
    //endregion Singleton

    //region Attributes
    private Dialog dialog;
    private Context context;
    //endregion Attributes

    //region Actions
    public void showDialog(Enums.DialogType dialogType) {

        context = AppController.getInstance().getCurrentActivity();
        if (!(context instanceof Activity)) {
            return;
        }

        switch (dialogType) {
            case ACKNOWLEDGEMENT:
                new Acknowledgement(context);
                break;
            case CONFIRMATION:
                new Confirmation(context);
                break;
            case INFORMATION:
                new Information(context);
                break;
            case WARNING:
                new Warning(context);
                break;
            case NOINTERNET:
                new NoInternet(context);
                break;
            default:
                return;
        }
    }

    @Override
    public void Initialize() {
        setInitialized(true);
    }
    //endregion Actions


}
