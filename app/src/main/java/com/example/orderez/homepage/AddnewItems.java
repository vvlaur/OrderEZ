package com.example.orderez.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.orderez.BackKeyHandler;
import com.example.orderez.DatabaseManager;
import com.example.orderez.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddnewItems extends AppCompatActivity{
    private Button submit_Btn;
    private EditText title, memo, amount;
    private Spinner  oneTimeConsumeUnit;
    private String id;
    private DatabaseManager theDb;
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);


    Calendar pickDates = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener fromDataPicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            pickDates.set(Calendar.YEAR,year);
            pickDates.set(Calendar.MONTH,month);
            pickDates.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_items);


        theDb = new DatabaseManager(this);
        title = (EditText) findViewById(R.id.addItem_title);
        memo = (EditText) findViewById(R.id.addItem_memo);
        amount = (EditText) findViewById(R.id.addItem_oneTimeConsume);

        Intent intent = getIntent();
        id = intent.getStringExtra("userId"); //user_id from previous activity using Intent

        EditText et_from_Date = (EditText) findViewById(R.id.from_Date);
        et_from_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddnewItems.this, fromDataPicker, pickDates.get(Calendar.YEAR), pickDates.get(Calendar.MONTH), pickDates.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        oneTimeConsumeUnit = (Spinner) findViewById(R.id.addItem_unitPer01);
        ArrayAdapter unitAdapter = ArrayAdapter.createFromResource(this,R.array.UnitTypes,android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        oneTimeConsumeUnit.setAdapter(unitAdapter);
        String unitOne = oneTimeConsumeUnit.getSelectedItem().toString();


        submit_Btn = (Button) findViewById(R.id.submit_schedule);
        submit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Grabs Texts from EditText, Stores into String

                String str_title = title.getText().toString();
                String str_amount = amount.getText().toString();
                String str_unit = oneTimeConsumeUnit.getSelectedItem().toString();

                String str_memo = memo.getText().toString();
                String date = et_from_Date.getText().toString();
                Boolean added = false;

                //Error Handlers
                if (str_title.length() > 100 ||str_memo.length() >100|| date.length() > 100) {
                    Toast.makeText(getApplicationContext(), "Please enter no more than 100 words", Toast.LENGTH_LONG).show();
                }else if ( str_amount.length() > 10 ){
                    Toast.makeText(getApplicationContext(), "Please enter amount no more than 10 digits", Toast.LENGTH_LONG).show();
                }else if (!str_title.equals("") && !str_amount.equals("") && !str_unit.equals("Select one") && !date.equals("")){
                    added = theDb.insertItem(str_title, str_amount, str_unit, date, str_memo, id);
                }else{
                    Toast.makeText(getApplicationContext(), "Please Add Required Information!", Toast.LENGTH_LONG).show();
                }

                if (added == true){
                    Toast.makeText(getApplicationContext(), "Data Added", Toast.LENGTH_LONG).show();

                    Intent goBacktoHomepage = new Intent(getApplicationContext(), Homepage_Items.class);
                    goBacktoHomepage.putExtra("userId", id);
                    startActivity(goBacktoHomepage);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Data Not Added", Toast.LENGTH_LONG).show();
                }

            }
        });
        if (savedInstanceState != null) {
            String emailContinue = savedInstanceState.getString("forgot_email");
            title.setText(emailContinue);
            String passwordContinue = savedInstanceState.getString("forgot_password");
            memo.setText(passwordContinue);
            String passwordVersifyContinue = savedInstanceState.getString("forgot_verifyPassword");
            amount.setText(passwordVersifyContinue);
        }

    }// onCreate

    @Override
    protected  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title",title.getText().toString());
        outState.putString("memo",memo.getText().toString());
        outState.putString("amount",amount.getText().toString());
    }
    private void updateLabel(){
        String format_default = "MM/dd/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(format_default, Locale.US);

        EditText et_from_date = (EditText) findViewById(R.id.from_Date);
        et_from_date.setText(formatter.format(pickDates.getTime()));
    }


    //Overrides BackKeyHandler's onBackPressed method
    //Finish the app if user clicks the back button twice in 2 seconds.
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed_BacktoItem("You will lose your progress if you click back button again.");
    }
}
//Class