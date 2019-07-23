package com.scanlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jhansi on 05/04/15.
 */
public class Utils {

    private Utils() {

    }

    static Uri getUri(Context context, Bitmap bitmap) throws FileNotFoundException {
        File mFile = new File(String.valueOf(context.getCacheDir()));
        System.out.println("mFile "+mFile);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),mFile.getAbsolutePath(), "NewImage", "Cropped one");
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "NewImage", null);

        System.out.println("path "+path);

        return Uri.parse(path);
    }

    static Bitmap getBitmap(Context context, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}