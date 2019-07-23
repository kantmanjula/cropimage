package com.scanner.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.scanlibrary.ResultFragment;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;


public class MainActivity extends AppCompatActivity implements ScanActivity.IScannedCropImage {

    private static final int REQUEST_CODE = 99;
    private Button scanButton;
    private Button cameraButton;
    private Button mediaButton;
    private ImageView scannedImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new ScanButtonClickListener());
        cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        mediaButton = (Button) findViewById(R.id.mediaButton);
        mediaButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_MEDIA));
        scannedImageView = (ImageView) findViewById(R.id.scannedImage);
        ScanActivity.setOnHeadlineSelectedListener(this);
    }

    @Override
    public void onCropFinish(Bitmap bitmap) {
        System.out.println("Bitmap xxxxxxxxxx "+bitmap);
        scannedImageView.setImageBitmap(bitmap);
    }


    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String message = data.getExtras().getString(ScanConstants.SCANNED_RESULT);
            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
