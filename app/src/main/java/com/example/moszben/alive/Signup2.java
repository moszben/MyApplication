package com.example.moszben.alive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Signup2 extends AppCompatActivity {

    ProgressDialog dialog;
    Spinner spinner;
    ArrayAdapter adapter;
    EditText reference;
    String ip,nom,prenom,user,pass,model,ref;
    boolean hasConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        Intent i=getIntent();
        Bundle ex=i.getExtras();
        ip=ex.getString("ip");
        nom=ex.getString("nom");
        prenom=ex.getString("prenom");
        user=ex.getString("user");
        pass=ex.getString("pass");
        dialog=new ProgressDialog(this);

        reference=(EditText)findViewById(R.id.ref);
        spinner=(Spinner)findViewById(R.id.spinner);
        String [] models=getResources().getStringArray(R.array.models);
        adapter=new ArrayAdapter(getApplicationContext(),R.layout.spinner_item,R.id.model,models);
        spinner.setAdapter(adapter);
    }

    public void onLogin(View v)
    {
        Intent i=new Intent(getApplication(),LoginAct.class);
        finish();
        startActivity(i);
    }

    private class SignupTask extends AsyncTask<String,Void,String> {
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
                String s = "nom=" + nom + "&prenom=" + prenom + "&user=" + user + "&pass=" + pass + "&model=" + model + "&ref=" + ref;

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
            String ok = "client ajouté\n";

            if (s.equals(ok)) {
                Toast.makeText(getApplicationContext(),R.string.saved,Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplication(),LoginAct.class);
//                i.putExtra("user",user);
//                i.putExtra("pass",pass);
                finish();
                startActivity(i);
//                Toast.makeText(getApplicationContext(), "Infos correctes", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.infosError, Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.clear) {
            spinner.setSelection(0);
            reference.setText("");
            return true;
        }else if (id == R.id.ok) {
            model=spinner.getSelectedItem().toString();
            ref=reference.getText().toString();

            if(ref.isEmpty())
                reference.setError("!");
            else{
                hasConnection = haveNetworkConnection();
                if (hasConnection) {
                    String urlString = "http://" + ip + "/signup.php";
                    SignupTask signupTask = new SignupTask();
                    signupTask.execute(urlString);
                    dialog.setTitle(R.string.wait);
                    dialog.show();
                } else
                {
                    Toast.makeText(getBaseContext(), R.string.noCnx, Toast.LENGTH_SHORT).show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
