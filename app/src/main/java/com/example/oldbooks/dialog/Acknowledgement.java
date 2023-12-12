package com.example.oldbooks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import com.example.oldbooks.databinding.DialogAcknowledgementBinding;

public class Acknowledgement extends Dialog {

    private DialogAcknowledgementBinding diaBinding;
    private Context context;

    public Acknowledgement(@NonNull Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    private void initialize() {
        diaBinding = DialogAcknowledgementBinding.inflate(LayoutInflater.from(context));
        setContentView(diaBinding.getRoot());
        setCancelable(true);
    }
}
