package com.scanlibrary;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by jhansi on 04/04/15.
 */
public interface IScanner {

    void onBitmapSelect(Uri uri);

    void onScanFinish(Bitmap bitmap);

    void onCroppedFinish(Bitmap bitmap);
}
