package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

// By Naser Kakakdost
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true;
    TextView changeSignupModeTextView;
    EditText passwordEditText;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){

            signUp(v);
        }

        return false;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.changeSignupModeTextView) {

            Button signupButton = (Button) findViewById(R.id.signupButton);

            Log.i("change signup mode", "change signup mode");


            if (signUpModeActive) {

                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Or, Signup");

            } else {

                signUpModeActive = true;
                signupButton.setText("Signup");
                changeSignupModeTextView.setText("Or, Login");
            }
        }else if (v.getId() == R.id.backgroundConstraintLayout || v.getId() == R.id.logoImageView) { // close down keyboard when click

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void signUp(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {


                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup", "Successful");

                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Signup", "Login successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);
        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        changeSignupModeTextView.setOnClickListener(this);

        ConstraintLayout backgroundRelativeLayout = (ConstraintLayout) findViewById(R.id.backgroundConstraintLayout);
        backgroundRelativeLayout.setOnClickListener(this);
        ImageView logoImageView =(ImageView) findViewById(R.id.logoImageView);
        logoImageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }
    }


}
