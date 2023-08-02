package com.example.sindikatzajedno;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class KarticaActivity extends AppCompatActivity {
    String sIme,sPrezime,sKartica,sOib;
    TextView tvIme,tvKartica,tvOvdje;
    ImageView img;
    long doubleClickLastTime = 0L;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_kartica);
        tvIme =(TextView)findViewById(R.id.tVime);
        tvKartica =(TextView)findViewById(R.id.tvSerijskiBroj);
        tvOvdje=(TextView)findViewById(R.id.textOvdje);
        img=(ImageView)findViewById(R.id.imageView);
        ctx=this;

        Intent intent=getIntent();
        intent.getExtras();

        if(intent.hasExtra("IME"))
        {
           sIme =intent.getStringExtra("IME");
        }
        if(intent.hasExtra("PREZIME"))
        {
            sPrezime=intent.getStringExtra("PREZIME");
        }
        if(intent.hasExtra("KARTICA"))
        {
            sKartica=intent.getStringExtra("KARTICA");
        }
        if(intent.hasExtra("OIB"))
        {
            sOib=intent.getStringExtra("OIB");
        }

        tvIme.setText(sIme +" "+sPrezime);
        tvKartica.setText(sKartica);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - doubleClickLastTime < 300){
                    doubleClickLastTime = 0;
                    Log.d("testiram","unutra");
                    Log.d("testiram", img.getTag().toString());

                    if(img.getTag().toString().equals("prednja")){
                        img.setTag("zadnja");
                        img.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zajednoback));
                        tvIme.setVisibility(View.GONE);
                        tvKartica.setVisibility(View.GONE);
                    }else if(img.getTag().toString().equals("zadnja")){
                        img.setTag("prednja");
                        img.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zajednofront));
                        tvIme.setVisibility(View.VISIBLE);
                        tvKartica.setVisibility(View.VISIBLE);
                    }





                    Log.d("testiram", img.getTag().toString());


                }else{
                    doubleClickLastTime = System.currentTimeMillis();
                }
            }
        });

      tvOvdje.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              AlertDialog.Builder alert = new AlertDialog.Builder(ctx, R.style.AlertDialogTheme);
              alert.setTitle("Sindikat Zajedno");

              WebView wv = new WebView(ctx);
              wv.loadUrl("https://www.sindikat-zajedno.hr/pogodnosti-za-clanove/");
              wv.setWebViewClient(new WebViewClient() {
                  @Override
                  public boolean shouldOverrideUrlLoading(WebView view, String url) {
                      view.loadUrl(url);

                      return true;
                  }
              });

              alert.setView(wv);
              alert.setNegativeButton("Nazad", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int id) {
                      dialog.dismiss();
                  }
              });
              alert.show();
          }
      });
    }

}