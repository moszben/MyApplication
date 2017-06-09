package com.example.moszben.alive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginAct extends AppCompatActivity {

    ProgressDialog dialog;
    String ip,user,pass;
    boolean hasConnection;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pass);
        dialog=new ProgressDialog(this);

        //entrer l'@ ip du votre server
        ip="192.168.1.4";

        SharedPreferences shrd=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String users=shrd.getString("username", "");
        String passs=shrd.getString("password", "");
    }

    //pour eviter la verification d username et mdp a chak fois
    //c temporaire
    public void onLogin(View v)
    {
        Intent i = new Intent(getApplication(), Home.class);
        i.putExtra("ip", ip);
        finish();
        startActivity(i);
    }
    ////////////////////////////////
    public void onLog(View v)
    {
        user=username.getText().toString();
        pass=password.getText().toString();

        if (user.isEmpty()) {
            username.setError("!");
            Toast.makeText(getBaseContext(), R.string.userEmpty, Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            password.setError("!");
            Toast.makeText(getBaseContext(), R.string.passEmpty, Toast.LENGTH_SHORT).show();
        } else {
            hasConnection = haveNetworkConnection();
            if (hasConnection) {
                String urlString = "http://" + ip + "/login.php";
                LoginTask loginTask = new LoginTask();
                loginTask.execute(urlString);
                dialog.setTitle(R.string.wait);
                dialog.show();
            } else
            {
                Toast.makeText(getBaseContext(), R.string.noCnx, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoginTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection c = null;
            try {
                String urlString = params[0];
                URL url = new URL(urlString);
                c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("POST");
                c.setConnectTimeout(15000 /* milliseconds */);
                c.setDoInput(true);
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String s = "user=" + user + "&pass=" + pass;

                c.setFixedLengthStreamingMode(s.getBytes().length);
                PrintWriter out = new PrintWriter(c.getOutputStream());
                out.print(s);
                out.close();

                c.connect();
                int mStatusCode = c.getResponseCode();
                switch (mStatusCode) {
                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        return sb.toString();
                }
                return "";
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return "Error connecting to server";
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String ok = "ok\n";

            if (s.equals(ok)) {
                SharedPreferences share=getSharedPreferences("userInfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=share.edit();
                ed.putString("username",user);
                ed.putString("password",pass);
                ed.apply();
                ///////////////
                Intent i = new Intent(getApplication(), Home.class);
                i.putExtra("ip", ip);
                finish();
                startActivity(i);
//                Toast.makeText(getApplicationContext(), "Infos correctes", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.infosError, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onSign(View v)
    {
        Intent i=new Intent(getApplication(),Signup.class);
        i.putExtra("ip",ip);
        finish();
        startActivity(i);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
