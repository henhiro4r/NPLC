package com.uc.nplc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.Result;
import com.uc.nplc.preference.Pref;
import com.uc.nplc.viewmodel.ScanViewModel;

import org.jetbrains.annotations.NotNull;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PlayScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String user_id = "";
    private ProgressDialog pd;
    private ScanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_scanner);
        pd = new ProgressDialog(PlayScanner.this);
        mScannerView = new ZXingScannerView(PlayScanner.this);
        Pref pref = new Pref(this);
        user_id = pref.getIdKey();
        setContentView(mScannerView);
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Scan QR Code");
        }

        viewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
    }

    @Override
    public void handleResult(Result result) {
        final String posQR = result.getText();
        new AlertDialog.Builder(PlayScanner.this)
                .setTitle("Confirmation")
                .setMessage("Are sure want to continue ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pd.setCancelable(false);
                        pd.setTitle("Processing");
                        pd.setMessage("Please wait...");
                        pd.show();
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                scan(posQR);
                            }
                        };
                        Handler cancel = new Handler();
                        cancel.postDelayed(r, 2000);
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onResume();
                    }
                })
                .create()
                .show();
    }

    private void scan(String code) {
        viewModel.play(code, user_id);
        viewModel.getMessage().observe(this, checkPlay);
        finish();
    }

    private Observer<String> checkPlay = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            if  (!message.isEmpty()) {
                showMessage(message);
                pd.cancel();
            }
        }
    };

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
