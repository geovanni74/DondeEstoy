package com.example.dondeestoy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

   // TextView txtGPS;
    LocationManager GPS;
    MediaPlayer localizacion;
    ConstraintLayout fondo;
    AlertDialog Aviso;
    AlertDialog Aviso2;
    SmsManager sms= SmsManager.getDefault();
    boolean smsEnviado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fondo = (ConstraintLayout) findViewById(R.id.fondo);
        localizacion = MediaPlayer.create(getBaseContext(), R.raw.localizacion);
        fondo.setBackgroundColor(Color.BLACK);
        Aviso = new AlertDialog.Builder(this).create();
        Aviso2 = new AlertDialog.Builder(this).create();
        smsEnviado=false;

        if (!localizacion.isPlaying()) {
            localizacion.start();
        }
            //txtGPS = (TextView) findViewById(R.id.txtGPS);

        try {
            GPS = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            GPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
        }catch(Exception e){
           // txtGPS.setText("El error es: "+e.getMessage());
        }

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        // txtGPS.setBackgroundColor(Color.GREEN);
        // txtGPS.setText("Recibiendo información del GPS");
    }

    @Override
    public void onProviderDisabled(String provider) {

        //txtGPS.setBackgroundColor(Color.RED);
        //txtGPS.setText("Por favor enciende tu GPS");
        Aviso.setTitle("Error de Localización");
        Aviso.setMessage("Favor de encender su GPS :)");
        Aviso.show();
    }

    @Override
    public void onLocationChanged(Location location) {

        Location hogar = new Location("Casa" );

        hogar.setLatitude(19.47435);
        hogar.setLongitude(-99.04606166666666);

        Location abandono = new Location("Abandono" );

        abandono.setLatitude(location.getLatitude());
        abandono.setLongitude(location.getLongitude());

        double distancia = hogar.distanceTo(abandono);

        if(distancia>10 && smsEnviado==false){
            Aviso2.setTitle("Desgraciada");
            Aviso2.setMessage("Regresa a tu corral :(");
            Aviso2.show();
            sms.sendTextMessage("5513894675",null,"Geovanni dice: Zorro no te la lleves!!! :(",null,null);
            smsEnviado=true;
        }
       /* try {
            txtGPS.setText(
                            "\nAltitud: " + location.getAltitude() +
                            "\nLongitud: " + location.getLongitude() +
                                    "\nLatitud: " + location.getLatitude()+
                                    "\nVelocidad: "+location.getSpeed());
            location.getAltitude();
            location.getLongitude();
            location.getLatitude();
        }catch (Exception e){
            txtGPS.setText("El error es: "+e.getMessage());
        }*/
    }
}
