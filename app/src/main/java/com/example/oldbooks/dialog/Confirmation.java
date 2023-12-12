package com.example.oldbooks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.oldbooks.R;
import com.example.oldbooks.databinding.DialogAcknowledgementBinding;
import com.example.oldbooks.databinding.DialogConfirmationBinding;

public class Confirmation extends Dialog {

    private DialogConfirmationBinding diaBinding;
    private Context context;

    public Confirmation(@NonNull Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    private void initialize() {
        diaBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(context));
        setContentView(diaBinding.getRoot());
        setCancelable(true);
    }
}
