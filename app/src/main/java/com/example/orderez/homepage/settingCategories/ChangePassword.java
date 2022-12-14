package com.example.orderez.homepage.settingCategories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderez.DatabaseManager;
import com.example.orderez.R;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class ChangePassword extends AppCompatActivity {

    DatabaseManager theDb;
    Cursor cursor;
    EditText oldPassword, newPassword, verifyNewPassword;
    Button submit;
    String id, var1, var2, var3;
    Boolean added=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        verifyNewPassword = (EditText) findViewById(R.id.verify_new_password);
        submit = (Button) findViewById(R.id.submit_reset);
        theDb = new DatabaseManager(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("userId"); //UserId from previous activity

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = theDb.searchId(id); //Searches account using id
                if (cursor.getCount()<=0){ //If no account is found

                    Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_LONG).show();
                }
                else if (cursor.moveToFirst() && cursor != null) { //If account is found

                    String pw = cursor.getString(cursor.getColumnIndexOrThrow("password")); //Encrypted password from database
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email")); //Email from database

                    //Error Handlers
                    if (!oldPassword.getText().toString().equals("") && !newPassword.getText().toString().equals("") && !verifyNewPassword.getText().toString().equals("")) {
                        try {
                            String decrypted = AESCrypt.decrypt(oldPassword.getText().toString(), pw);
                            if (decrypted.equals(email)) {
                                if (!(4<= newPassword.getText().length() && newPassword.getText().length()<=20) || !(4<= verifyNewPassword.getText().length() && verifyNewPassword.getText().length()<=20)){
                                    Toast.makeText(getApplicationContext(), "New Password Should be Between 4~20 Characters!", Toast.LENGTH_LONG).show();
                                }
                                else if (!newPassword.getText().toString().equals(verifyNewPassword.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "New Passwords Do Not Match!", Toast.LENGTH_LONG).show();
                                }else if (oldPassword.getText().toString().length()>100 ){
                                    Toast.makeText(getApplicationContext(), "Too Much Information!", Toast.LENGTH_LONG).show();
                                } else {
                                    added = theDb.updatePW(id, email, newPassword.getText().toString()); // Passes Error Handlers
                                }


                                if (added == true) {
                                    Toast.makeText(getApplicationContext(), "Successfully Updated Password", Toast.LENGTH_LONG).show();
                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to Update Password", Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (GeneralSecurityException e) {
                            //handle error - could be due to incorrect password or tampered encryptedMsg
                            Toast.makeText(getApplicationContext(), "Old Password Does Not Match!", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Required Information!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });



    }
}