package it.jaschke.alexandria;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerFragment extends Fragment implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        ArrayList<BarcodeFormat> format = new ArrayList<>();
        format.add(BarcodeFormat.EAN13);
        mScannerView.setFormats(format);
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCamera();
        Bundle arguments = new Bundle();
        arguments.putString("Contents", rawResult.getContents());
        arguments.putString("Format", rawResult.getBarcodeFormat().getName());
        FragmentManager fragmentManager = getFragmentManager();
        Fragment addBook= new AddBook();
        addBook.setArguments(arguments);
        fragmentManager.beginTransaction().replace(R.id.container,addBook).addToBackStack(null).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


}
