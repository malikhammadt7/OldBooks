package com.example.oldbooks.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.oldbooks.R;
import com.example.oldbooks.databinding.DialogInformationBinding;
import com.example.oldbooks.databinding.DialogWarningBinding;

public class Warning extends Dialog {

    private DialogWarningBinding diaBinding;
    private Context context;

    public Warning(@NonNull Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    private void initialize() {
        diaBinding = DialogWarningBinding.inflate(LayoutInflater.from(context));
        setContentView(diaBinding.getRoot());
        setCancelable(true);

        this.show();
    }
}