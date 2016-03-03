package user.emertonom.cardboardprofileviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileInputStream fstream = null;
        String extDir  = Environment.getExternalStorageDirectory().getPath();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
            Log.i("emertonom", "External storage is mounted read/write");
        }
        String filePath = extDir + "/Cardboard/current_device_params";

        Log.i("emertonom", "Parameter path is " + filePath);

        try {
            File file = new File(filePath);
            if (file.exists()) {
                Log.i("emertonom", "File Exists");
                if (file.canRead()) {
                    Log.i("emertonom", "File is readable");
                } else {
                    Log.e("emertonom", "Can't read file");
                }
            } else {
                Log.e("emertonom", "File Doesn't Exist");
            }
            fstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            DialogInterface.OnClickListener listener = new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Error");
            dlg.setMessage("Unable to locate Cardboard Device Parameters");
            dlg.setCancelable(false);
            dlg.setPositiveButton("Exit", listener);
            dlg.create();
            dlg.show();
        }

        if (fstream == null) {
            Log.e("emertonom", "fstream still null");
        } else {
            Log.i("emertonom", "fstream successfully set");
        }
        CardboardDevice.DeviceParams deviceParams = null;

        try {
            if (fstream != null) {
                fstream.skip(7);
                deviceParams = CardboardDevice.DeviceParams.parseDelimitedFrom(fstream);
            }
        } catch (IOException e) {
            DialogInterface.OnClickListener listener = new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };

            Log.e("emertonom", e.toString());

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Error");
            dlg.setMessage("Unable to read Cardboard Device Parameters, IO Exception");
            dlg.setCancelable(false);
            dlg.setPositiveButton("Exit", listener);
            dlg.create();
            dlg.show();
        }

        if (deviceParams != null) {

            // vendorText
            if (deviceParams.hasVendor()) {
                TextView vendorText = (TextView) findViewById(R.id.vendorText);
                vendorText.setText(deviceParams.getVendor());
            }

            // modelText
            if (deviceParams.hasModel()) {
                TextView modelText = (TextView) findViewById(R.id.modelText);
                modelText.setText(deviceParams.getModel());
            }

            // screenToLensText
            if (deviceParams.hasScreenToLensDistance()) {
                TextView screenToLensText = (TextView) findViewById(R.id.screenToLensText);
                float screenToLensFloat = deviceParams.getScreenToLensDistance();
                screenToLensText.setText(screenToLensFloat * 1000 + "mm");
            }

            // interlensText
            if (deviceParams.hasInterLensDistance()) {
                TextView interlensText = (TextView) findViewById(R.id.interlensText);
                float interlensFloat = deviceParams.getInterLensDistance();
                interlensText.setText(interlensFloat * 1000 + "mm");
            }

            // verticalAlignmentText
            if (deviceParams.hasVerticalAlignment()) {
                TextView verticalAlignmentText = (TextView) findViewById(R.id.verticalAlignmnentText);
                CardboardDevice.DeviceParams.VerticalAlignmentType verticalAlignment =
                        deviceParams.getVerticalAlignment();
                switch (verticalAlignment) {
                    case BOTTOM:
                        verticalAlignmentText.setText("BOTTOM");
                        break;
                    case CENTER:
                        verticalAlignmentText.setText("CENTER");
                        break;
                    case TOP:
                        verticalAlignmentText.setText("TOP");
                        break;
                    default:
                        verticalAlignmentText.setText("NONE");
                        break;

                }

            }

            // trayToLensText
            if (deviceParams.hasTrayToLensDistance()) {
                TextView trayToLensText = (TextView) findViewById(R.id.trayToLensText);
                float trayToLensFloat = deviceParams.getTrayToLensDistance();
                trayToLensText.setText(trayToLensFloat * 1000 + "mm");
            }

            // hasMagnetText
            if (deviceParams.hasHasMagnet()) {
                TextView hasMagnetText = (TextView) findViewById(R.id.hasMagnetText);
                if (deviceParams.getHasMagnet()) {
                    hasMagnetText.setText("TRUE");
                } else {
                    hasMagnetText.setText("FALSE");
                }
            }

            // buttonTypeText
            if (deviceParams.hasPrimaryButton()) {
                TextView buttonTypeText = (TextView) findViewById(R.id.buttonTypeText);
                CardboardDevice.DeviceParams.ButtonType buttonType =
                        deviceParams.getPrimaryButton();
                switch (buttonType) {
                    case NONE:
                        buttonTypeText.setText("NONE");
                        break;
                    case TOUCH:
                        buttonTypeText.setText("TOUCH");
                        break;
                    case MAGNET:
                        buttonTypeText.setText("MAGENET");
                        break;
                    case INDIRECT_TOUCH:
                        buttonTypeText.setText("INDIRECT TOUCH");
                        break;
                    default:
                        buttonTypeText.setText("UNKNOWN");
                        break;
                }
            }

            // k1Text
            // k2Text
            List<Float> distortions = deviceParams.getDistortionCoefficientsList();
            if (distortions.size() >= 2) {
                TextView k1Text = (TextView) findViewById(R.id.k1Text);
                TextView k2Text = (TextView) findViewById(R.id.k2Text);
                float k1Float = distortions.get(0);
                float k2Float = distortions.get(1);
                k1Text.setText(k1Float + "");
                k2Text.setText(k2Float + "");
            }

        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://user.emertonom.cardboardprofileviewer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://user.emertonom.cardboardprofileviewer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
