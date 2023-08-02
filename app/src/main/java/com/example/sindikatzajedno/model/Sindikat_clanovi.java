package com.example.sindikatzajedno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Sindikat_clanovi {
    @SerializedName("ime")
    @Expose
    private String ime;
    @SerializedName("prezime")
    @Expose
    private String prezime;
    @SerializedName("datum")
    @Expose
    private String datum;
    @SerializedName("adresa")
    @Expose
    private String adresa;
    @SerializedName("oib")
    @Expose
    private String oib;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobitel")
    @Expose
    private String mobitel;
    @SerializedName("ustanovaRada")
    @Expose
    private String ustanovaRada;
    @SerializedName("sifra")
    @Expose
    private String sifra;
    @SerializedName("aktivacija")
    @Expose
    private String aktivacija;

    @SerializedName("kartica")
    @Expose
    private String kartica;

    public String getKartica() {
        return kartica;
    }

    public void setKartica(String kartica) {
        this.kartica = kartica;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobitel() {
        return mobitel;
    }

    public void setMobitel(String mobitel) {
        this.mobitel = mobitel;
    }

    public String getUstanovaRada() {
        return ustanovaRada;
    }

    public void setUstanovaRada(String ustanovaRada) {
        this.ustanovaRada = ustanovaRada;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getAktivacija() {
        return aktivacija;
    }

    public void setAktivacija(String aktivacija) {
        this.aktivacija = aktivacija;
    }
}
