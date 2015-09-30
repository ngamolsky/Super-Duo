package it.jaschke.alexandria.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlexandriaSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static AlexandriaSyncAdapter sAlexandriaSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("AlexandriaSyncService", "onCreate - AlexandriaSyncService");
        synchronized (sSyncAdapterLock) {
            if (sAlexandriaSyncAdapter == null) {
                sAlexandriaSyncAdapter = new AlexandriaSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sAlexandriaSyncAdapter.getSyncAdapterBinder();
    }
}
