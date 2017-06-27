package com.kniffina.android.facebookposts;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.kniffina.android.facebookposts",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.v("catch 1", e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.v("catch 2", e.toString());
        }
        FacebookSdk.sdkInitialize(getApplicationContext());

        //if user has acccessToken automatically send them to the main menu
        if(AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(MainActivity.this, CreateEditDelete.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("user_posts"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));

                //on success of the login send them to the CreateEditDelete activity
                Intent intent = new Intent(MainActivity.this, CreateEditDelete.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                String errorMessage = "Error: Login was cancelled";
                Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                String errorMessage = "Error: Something went wrong, please wait a moment and try again";
                Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}