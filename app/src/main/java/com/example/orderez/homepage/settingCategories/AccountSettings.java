package com.example.orderez.homepage.settingCategories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderez.DatabaseManager;
import com.example.orderez.R;
import com.example.orderez.intro.WelcomePage;

public class AccountSettings extends AppCompatActivity {

    TextView welcome, email;
    DatabaseManager theDb;
    Cursor cursor;
    String id, var1, var2, var3;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        welcome = (TextView) findViewById(R.id.welcome);
        email = (TextView) findViewById(R.id.email);
        logout = (Button) findViewById(R.id.logout);

        theDb = new DatabaseManager(this);


        Intent intent = getIntent();
        id = intent.getStringExtra("userId");

        cursor =theDb.searchId(id); //Search account using userid
        if (cursor.getCount()<=0){  // If no account is found

            Toast.makeText(getApplicationContext(), "Failed To Load Data!", Toast.LENGTH_LONG).show();
        }
        else if (cursor.moveToFirst() && cursor != null) { // If account is found

            var1 = cursor.getString(cursor.getColumnIndexOrThrow("first"));
            var2 = cursor.getString(cursor.getColumnIndexOrThrow("last"));
            var3 = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            welcome.setText("Welcome " + var1 + " " + var2 + "!"); //Sets welcome text
            email.setText(var3+"");

        }
        logout.setOnClickListener(new View.OnClickListener() { //Logout Function
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //Finishes all activity except the welcome activity
                startActivity(intent);
            }
        });








    }
}