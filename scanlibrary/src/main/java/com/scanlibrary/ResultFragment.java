package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jhansi on 29/03/15.
 */
public class ResultFragment extends Fragment {

    private View view;
    private ImageView scannedImageView;
    private Button doneButton;
    private Bitmap original;
    private Button originalButton;
    private Button MagicColorButton;
    private Button grayModeButton;
    private Button bwButton;
    private Bitmap transformed;
    private static ProgressDialogFragment progressDialogFragment;
    public IScanner scanner;

    public ResultFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof IScanner)) {
            throw new ClassCastException("Activity must implement IScanner");
        }
        this.scanner = (IScanner) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_layout, null);
        init();
        return view;
    }

    private void init() {
        scannedImageView = (ImageView) view.findViewById(R.id.scannedImage);
        originalButton = (Button) view.findViewById(R.id.original);
        originalButton.setOnClickListener(new OriginalButtonClickListener());
        MagicColorButton = (Button) view.findViewById(R.id.magicColor);
        MagicColorButton.setOnClickListener(new MagicColorButtonClickListener());
        grayModeButton = (Button) view.findViewById(R.id.grayMode);
        grayModeButton.setOnClickListener(new GrayButtonClickListener());
        bwButton = (Button) view.findViewById(R.id.BWMode);
        bwButton.setOnClickListener(new BWButtonClickListener());
        Bitmap bitmap = getBitmap();
        original = bitmap;
        setScannedImage(bitmap);
        doneButton = (Button) view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new DoneButtonClickListener());
    }

/*    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = Utils.getBitmap(getActivity(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private Bitmap getBitmap() {
        byte[] byteArray = getArguments().getByteArray(ScanConstants.SCANNED_RESULT);
        return Utils.convertByteArrayToBitmap(byteArray);
    }

    public void setScannedImage(Bitmap scannedImage) {
        scannedImageView.setImageBitmap(scannedImage);
    }

    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showProgressDialog(getResources().getString(R.string.loading));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent data = new Intent();
                        Bitmap bitmap = transformed;
                        if (bitmap == null) {
                            bitmap = original;
                        }

                        data.putExtra(ScanConstants.SCANNED_RESULT, "Scanned Successfully");
                        getActivity().setResult(Activity.RESULT_OK, data);
                        final Bitmap finalBitmap = bitmap;

                     /*   original.recycle();
                        System.gc();*/

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scanner.onCroppedFinish(finalBitmap);
                                dismissDialog();
                                getActivity().finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class BWButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getBWBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    private class MagicColorButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    private class OriginalButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                showProgressDialog(getResources().getString(R.string.applying_filter));
                transformed = original;
                scannedImageView.setImageBitmap(original);
                dismissDialog();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                dismissDialog();
            }
        }
    }

    private class GrayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    protected synchronized void showProgressDialog(String message) {
        if (progressDialogFragment != null && progressDialogFragment.isVisible()) {
            // Before creating another loading dialog, close all opened loading dialogs (if any)
            progressDialogFragment.dismissAllowingStateLoss();
        }
        progressDialogFragment = null;
        progressDialogFragment = new ProgressDialogFragment(message);
        FragmentManager fm = getFragmentManager();
        progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
    }

    protected synchronized void dismissDialog() {
        progressDialogFragment.dismissAllowingStateLoss();
    }
}