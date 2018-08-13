package tw.edu.nctu.cs.nems.cellularsignal;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    public static String file = "/dev/null";
    public static Handler mainHandler;

    private TextView nt;
    private TextView ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request permissions
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
            Manifest.permission.READ_PHONE_STATE,  Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        // get arguments
        Bundle extras = this.getIntent().getExtras();
        if (extras != null && extras.containsKey("file")) {
            file = "/sdcard/cjyeh/cellularsignal/" + extras.getString("file");
        }

        // add service
        Intent intent = new Intent(this, LogService.class);
        this.startService(intent);

        nt = (TextView) findViewById(R.id.network_type);
        ss = (TextView) findViewById(R.id.signal_strength);

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                nt.setText((String) msg.obj);
                ss.setText(Integer.toString(msg.what))
            }
        };
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
