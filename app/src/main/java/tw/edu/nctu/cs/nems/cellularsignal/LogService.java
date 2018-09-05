package tw.edu.nctu.cs.nems.cellularsignal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.os.Handler;
import android.os.HandlerThread;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogService extends Service {
    private Handler handler = null;
    private HandlerThread handlerthread = null;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();

            Integer index = 0;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();

            // no radio connected
            if (cellInfos == null)
                return;

            while (index < cellInfos.size() && !cellInfos.get(index).isRegistered())
                index++;

            CellInfo cellInfo = cellInfos.get(index);

            if(cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                msg.obj = "WCDMA";
                msg.what = cellSignalStrengthWcdma.getDbm();

            }
            else if(cellInfo instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                msg.obj = "LTE";
                msg.what = cellSignalStrengthLte.getDbm();
            }

            Log.d("cjyeh", msg.obj + "\t" + msg.what);

            try {
                FileWriter fw = new FileWriter(MainActivity.file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(Integer.toString(msg.what));
                bw.newLine();
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.mainHandler.sendMessage(msg);

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerthread = new HandlerThread("");
        handlerthread.start();
        handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
