package com.example.oldbooks.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.oldbooks.R;
import com.example.oldbooks.databinding.DialogConfirmationBinding;
import com.example.oldbooks.databinding.DialogInformationBinding;

public class Information extends Dialog {

    private DialogInformationBinding diaBinding;
    private Context context;

    public Information(@NonNull Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    private void initialize() {
        diaBinding = DialogInformationBinding.inflate(LayoutInflater.from(context));
        setContentView(diaBinding.getRoot());
        setCancelable(true);
    }
}