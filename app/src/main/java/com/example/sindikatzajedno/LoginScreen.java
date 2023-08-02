package com.example.sindikatzajedno;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sindikatzajedno.model.Sindikat_clanovi;
import com.example.sindikatzajedno.retrofit.ApiInterface;
import com.example.sindikatzajedno.retrofit.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import spencerstudios.com.bungeelib.Bungee;

public class LoginScreen extends AppCompatActivity {
    Button btnRegistracija,btnPrijava;
    TextView txtLozinka;
    TextInputEditText tEdtEmail,tEdtSifra;
    CheckBox chk;
    Context ctx;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent=getIntent();
        intent.getExtras();

        if(intent.hasExtra("register"))
        {
            new GlideToast.makeToast(LoginScreen.this,"Registracija je uspješno napravljena",GlideToast.LENGTHTOOLONG,GlideToast.SUCCESSTOAST,GlideToast.BOTTOM).show();
        }
        btnRegistracija = (Button) findViewById(R.id.btnRegistracija);
        btnPrijava = (Button) findViewById(R.id.btnPrijava);

        tEdtEmail = (TextInputEditText) findViewById(R.id.tEdtEmail);
        tEdtSifra = (TextInputEditText) findViewById(R.id.tEdtSifra);
        txtLozinka =(TextView)findViewById(R.id.txtLozinka);
        chk =(CheckBox)findViewById(R.id.cZap);
        chk.setChecked(true);
        ctx=this;

        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginScreen.this, RegistretionScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Bungee.windmill(ctx);

            }
        });


        btnPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tEdtEmail.getText().toString()) && !TextUtils.isEmpty(tEdtSifra.getText().toString())){
                    dohvatiClanovaSindikata(tEdtEmail.getText().toString());
                }

            }
        });

        txtLozinka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tEdtEmail.getText().toString())){
                   posaljiEmailZaOporavak(tEdtEmail.getText().toString());
                }else{
                    tEdtEmail.setError("Nije upisana e-mail adresa za oporavak lozinke ");
                }

            }
        });
    }
    public void dohvatiClanovaSindikata(String eMail){
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        Retrofit retrofit = serviceGenerator.retrofit;
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<Sindikat_clanovi> call = api.getUsers("/clanovi/"+eMail);
        call.enqueue(new Callback<Sindikat_clanovi>() {
            @Override
            public void onResponse(Call<Sindikat_clanovi> call, Response<Sindikat_clanovi> response) {
                if(response.isSuccessful()){
                    Log.d("vidim",response.body().getEmail());

                    if(!response.body().getSifra().equals(tEdtSifra.getText().toString())){
                      //  Toast.makeText(LoginScreen.this, "Pogrešna lozinka,pokušajte ponovo", Toast.LENGTH_SHORT).show();
                        new GlideToast.makeToast(LoginScreen.this,"Pogrešna lozinka,pokušajte ponovo",GlideToast.LENGTHTOOLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();

                    }else{
                        if(response.body().getAktivacija().equals("N")){
                            new GlideToast.makeToast(LoginScreen.this,"Korisnički račun je u statusu aktivacije",GlideToast.LENGTHTOOLONG,GlideToast.WARNINGTOAST,GlideToast.BOTTOM).show();
                        }else{
                            Intent intent=new Intent(LoginScreen.this,KarticaActivity.class);
                            intent.putExtra("IME",response.body().getIme());
                            intent.putExtra("PREZIME",response.body().getPrezime());
                            intent.putExtra("OIB",response.body().getOib());
                            intent.putExtra("KARTICA",response.body().getKartica());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Bungee.windmill(ctx);
                        }
                    }



                }else{
                    new GlideToast.makeToast(LoginScreen.this,"Molim vas napravite registraciju , ne postoji račun sa ovom E-mail adresom",GlideToast.LENGTHTOOLONG,GlideToast.INFOTOAST,GlideToast.BOTTOM).show();
                }
            }

            @Override
            public void onFailure(Call<Sindikat_clanovi> call, Throwable t) {
                Log.d("vidim",t.getMessage());
                Toast.makeText(LoginScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void posaljiEmailZaOporavak(String eMail){
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        Retrofit retrofit = serviceGenerator.retrofit;
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<String> call = api.getLozinka("/lozinka/"+eMail);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String sTest= response.body();
                Log.d("test",sTest);
                if(response.isSuccessful()){
                    new GlideToast.makeToast(LoginScreen.this,"Lozinka je poslana na: "+tEdtEmail.getText().toString(),GlideToast.LENGTHTOOLONG,GlideToast.SUCCESSTOAST,GlideToast.BOTTOM).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("vidim",t.getMessage());
                Toast.makeText(LoginScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = tEdtEmail.getText().toString();
        PasswordValue = tEdtSifra.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

        if (TextUtils.isEmpty(UnameValue)){
            chk.setChecked(false);
        }

        tEdtEmail.setText(UnameValue);
        tEdtSifra.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(chk.isChecked()){
            savePreferences();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if(chk.isChecked()){
            loadPreferences();
        }

    }

}