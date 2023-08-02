package com.example.sindikatzajedno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sindikatzajedno.retrofit.ApiInterface;
import com.example.sindikatzajedno.retrofit.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistretionScreen extends AppCompatActivity {
    TextInputEditText tEdtDatRod,tEdtIme,tEdtPrezime,tEdtAdresa,tEdtOib,tEdtEmail,
                      tEdtMobitel,tEdtUstanovaRada,tEdtSifra,tEdtPonoviSifru;
    Button btnRegistracija;
    String sMobitel="",sEmail="";

    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registretion_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        datuRodenjaListener();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tEdtIme =  (TextInputEditText) findViewById(R.id.edtIme);
        tEdtPrezime = (TextInputEditText) findViewById(R.id.edtPrezime);
        tEdtAdresa = (TextInputEditText) findViewById(R.id.edtAdresa);
        tEdtOib = (TextInputEditText) findViewById(R.id.edtOib);
        tEdtEmail = (TextInputEditText) findViewById(R.id.edteMail);
        tEdtMobitel = (TextInputEditText) findViewById(R.id.edtMobitel);
        tEdtUstanovaRada =(TextInputEditText) findViewById(R.id.edtUstanovaRada);
        tEdtSifra =(TextInputEditText)findViewById(R.id.edtSifra);
        tEdtPonoviSifru = (TextInputEditText) findViewById(R.id.edtPonoviteSifru);
        btnRegistracija = (Button) findViewById(R.id.btnReg);

        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long mLastClickTime=0;
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                validacijaKodRegistracije();
            }
        });





    }

    public void datuRodenjaListener(){
        tEdtDatRod = (TextInputEditText) findViewById(R.id.edtDatRod);

        tEdtDatRod.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    tEdtDatRod.setText(current);
                    tEdtDatRod.setSelection(sel < current.length() ? sel : current.length());



                }
            }




            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    public void validacijaKodRegistracije()  {


        boolean bRegister = false;
        if(TextUtils.isEmpty(tEdtIme.getText().toString())){
            tEdtIme.setError("Upišite ime");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtPrezime.getText().toString())){
            tEdtPrezime.setError("Upišite prezime");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtDatRod.getText().toString())){
            tEdtDatRod.setError("Upišite datum rođenja");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtAdresa.getText().toString())){
            tEdtAdresa.setError("Upišite adresu");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtOib.getText().toString())){
            tEdtOib.setError("Upišite Oib");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtEmail.getText().toString())){
            tEdtEmail.setError("Upišite E-mail");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtMobitel.getText().toString())){
            tEdtMobitel.setError("Upišite mobitel");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtUstanovaRada.getText().toString())){
            tEdtUstanovaRada.setError("Upišite ustanovu rada");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtSifra.getText().toString())){
            tEdtSifra.setError("Upišite šifru");
            bRegister =true;
        }

        if(TextUtils.isEmpty(tEdtPonoviSifru.getText().toString())){
            tEdtPonoviSifru.setError("Upišite šifru");
            bRegister =true;
        }

        if(bRegister){
            return;
        }

        if(!OibValidation.checkOIB(tEdtOib.getText().toString())){
            new GlideToast.makeToast(RegistretionScreen.this,"OIB nije u ispravnom formatu",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
            return;
        }


        if(!TextUtils.isEmpty(tEdtMobitel.getText().toString())){
            sMobitel = tEdtMobitel.getText().toString().trim();


                if (!(sMobitel.charAt(0)=='0' && sMobitel.charAt(1)=='9' && (sMobitel.length() == 10 || sMobitel.length() ==9 )) ){
                    new GlideToast.makeToast(RegistretionScreen.this,"Broj mobitela nije u ispravnom formatu",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();

                    return;
                }


        }

        if(!TextUtils.isEmpty(tEdtEmail.getText().toString())){
            sEmail = tEdtEmail.getText().toString().trim().toLowerCase();
            EmailValidator validator = EmailValidator.getInstance();

            if (!validator.isValid(sEmail)){
                new GlideToast.makeToast(RegistretionScreen.this,"E-mail nije u ispravnom formatu",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
                return;
            }else{
                sEmail=sEmail.toLowerCase();
            }
        }else{
            sEmail="";
        }
        String isValid=isValid(tEdtSifra.getText().toString());

        if(!TextUtils.isEmpty(isValid)){
            new GlideToast.makeToast(RegistretionScreen.this,isValid,GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
            return;
        }

        if (!tEdtSifra.getText().toString().equals(tEdtPonoviSifru.getText().toString())) {
            new GlideToast.makeToast(RegistretionScreen.this,"Upisane lozinke nisu iste",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
            return;
        }





        try {
            insertNoviClan();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public void insertNoviClan()throws IOException {
        Log.d("testiran","unutra sam1");


        String greska = "Server Error," + " " + "status code:" + " ";
        String message;

        JsonObject jsonObject = new JsonObject();


        /** kad azuriram ne insertam nista u dogadaje kod unosa novog insettam */




        /**sif_obj ce biti sifra prodavaone koju cu spremit u shared preferences jel koristim u cijeloj aplikaciji */


        jsonObject.addProperty("ime", tEdtIme.getText().toString());
        jsonObject.addProperty("prezime", tEdtPrezime.getText().toString());
        jsonObject.addProperty("datum", formatDate(tEdtDatRod.getText().toString()));
        jsonObject.addProperty("adresa", tEdtAdresa.getText().toString());
        jsonObject.addProperty("oib", tEdtOib.getText().toString());
        jsonObject.addProperty("email", sEmail);
        jsonObject.addProperty("mobitel", tEdtMobitel.getText().toString());
        jsonObject.addProperty("ustanovaRada", tEdtUstanovaRada.getText().toString());
        jsonObject.addProperty("sifra", tEdtSifra.getText().toString());
        jsonObject.addProperty("aktivacija", "N");
        jsonObject.addProperty("kartica", "122322");



        Log.d("testiram","azturiranjeIinsert:1 "+jsonObject);






        ServiceGenerator serviceGenerator = new ServiceGenerator();
        Retrofit retrofit = serviceGenerator.retrofit;

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<String> call = api.registerClan(jsonObject);

        Response<String> result = call.execute();

            Log.d("testiram","azturiranjeIinsert:1 ");
            if (result.body() != null) {
                String status = result.body().toString();
                //String status = result.body().get("status").toString();
                Log.d("testiram","azturiranjeIinsert:2 ");
                Log.d("testiram",status);
                if (status.contains("OK")) {

                    Intent intent=new Intent(this,LoginScreen.class);
                    intent.putExtra("register","true");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    new GlideToast.makeToast(RegistretionScreen.this,status,GlideToast.LENGTHTOOLONG,GlideToast.INFOTOAST,GlideToast.BOTTOM).show();
                }


            } else {
                new GlideToast.makeToast(RegistretionScreen.this,"Greška kod kuminikacije sa serverom ",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
            }
    }

    public static String formatDate(String inDate) {
          SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yy");
          SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");
        String outDate = "";
        if (inDate != null) {
            try {
                Date date = inSDF.parse(inDate);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
            }
        }
        return outDate;
    }

    public static String isValid(String passwordhere) {

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (passwordhere.length() < 8) {

            return ("Lozinka mora sadržavati minimalno 8 znakova");

        }

        if (!UpperCasePatten.matcher(passwordhere).find()) {
            return ("Lozinka mora sadržavati barem jedno veliko slovo");


        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            return ("Lozinka mora sadržavati barem jedno malo slovo");

        }
        if (!digitCasePatten.matcher(passwordhere).find()) {

            return ("Lozinka mora sadržavati barem jedan broj");

        }
            return "";

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistretionScreen.this, LoginScreen.class));
        finish();
    }


}