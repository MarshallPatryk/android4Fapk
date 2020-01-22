package com.gdgbangla.scannerproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    public ZXingScannerView zXingScannerView;
    public TextView qrcode;
    public TextView wynikSzukaniaEdit;
    public EditText salonIDEdit;

    public String salonOFFICIAL;

    private RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_salon);

        //setContentView(R.layout.activity_main);
        qrcode  = (TextView) findViewById(R.id.wynikScan);
        wynikSzukaniaEdit  = (TextView) findViewById(R.id.wynikSzukania);
        salonIDEdit  = (EditText) findViewById(R.id.salonID);
        //qrcode.setText("Tttt");

        mQueue = Volley.newRequestQueue(this);
    }

    //wybieranie salonu
    //pobieranie bazy salonu
    //znajdz produkt po kodzie
    //znajdz model
    //wyswietl liste z produktami

    String url = "173.249.20.230:3000/product/";

    private void jsonParse(String kod) {

        String url = "http://173.249.20.230:3000/product/"+kod+"/?salon=gd12";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Log.d(s, "dane");
                qrcode  = (TextView) findViewById(R.id.wynikScan);
                qrcode.setText("test");
                try {
                    JSONObject obj = new JSONObject(s);
                    qrcode.setText(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                qrcode.setText(s);


            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Button btnaa  = (Button) findViewById(R.id.btnskan);
                Log.d(String.valueOf(volleyError),"Some error occurred!!");
                qrcode  = (TextView) findViewById(R.id.wynikScan);
                qrcode.setText("Some error occurred!!");
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    public void selectSalon(View view){

        salonOFFICIAL = String.valueOf(salonIDEdit.getText());
        Toast.makeText(this, salonOFFICIAL, Toast.LENGTH_LONG).show();
        //String[] dane = jsonParse(salonOFFICIAL);
        //qrcode.setText(dane[0]);
        setContentView(R.layout.activity_main);
    }

    public void scan(View view){
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_main); //stop camera
        jsonParse(result.getText());

        zXingScannerView.resumeCameraPreview(this);

    }
}
