package com.example.moszben.alive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    EditText name,fname,username,password,confirmpass;
    String ip,nom,prenom,user,pass,cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent i=getIntent();
        Bundle ex=i.getExtras();
        //username=ex.getString("username");
        ip=ex.getString("ip");

        name=(EditText)findViewById(R.id.name);
        fname=(EditText)findViewById(R.id.fname);
        username=(EditText)findViewById(R.id.suser);
        password=(EditText)findViewById(R.id.spass);
        confirmpass=(EditText)findViewById(R.id.cpass);
    }

    public void onLogin(View v)
    {
        Intent i=new Intent(getApplication(),LoginAct.class);
        finish();
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.clear) {
            name.setText("");
            fname.setText("");
            username.setText("");
            password.setText("");
            confirmpass.setText("");
            return true;
        }else if (id == R.id.ok) {
            nom=name.getText().toString();
            prenom=fname.getText().toString();
            user=username.getText().toString();
            pass=password.getText().toString();
            cpass=confirmpass.getText().toString();

            if(nom.isEmpty())
                name.setError("!");
            else if(prenom.isEmpty())
                fname.setError("!");
            else if(user.isEmpty())
                username.setError("!");
            else if(pass.isEmpty())
                password.setError("!");
            else if(cpass.isEmpty())
                confirmpass.setError("!");
            else if(!cpass.equals(pass))
                Toast.makeText(getApplicationContext(),R.string.passDiff, Toast.LENGTH_SHORT).show();
            else{
                Intent i=new Intent(getApplication(),Signup2.class);
                i.putExtra("ip",ip);
                i.putExtra("nom",nom);
                i.putExtra("prenom",prenom);
                i.putExtra("user",user);
                i.putExtra("pass",pass);
                finish();
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
