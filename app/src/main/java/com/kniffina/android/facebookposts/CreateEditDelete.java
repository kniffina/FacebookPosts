package com.kniffina.android.facebookposts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.LoginButton;

import static com.kniffina.android.facebookposts.R.id.main_menu_fb_login;

public class CreateEditDelete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete);

        //if there is no access token send them to the main page
        if(AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(CreateEditDelete.this, MainActivity.class);
            startActivity(intent);
        }

        LoginButton loginButton = (LoginButton) findViewById(main_menu_fb_login);

        //editPost onclick listener
        Button editPost = (Button) findViewById(R.id.edit_post_button);
        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEditDelete.this, EditPost.class);
                startActivity(intent);
            }
        });

        //create post button onClick Listener
        Button createPost = (Button) findViewById(R.id.create_post_button);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEditDelete.this, CreatePost.class);
                startActivity(intent);
            }
        });

        //delete post button onClick listener
        Button deletePost = (Button) findViewById(R.id.delete_post_button);
        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEditDelete.this, DeletePost.class);
                startActivity(intent);
            }
        });


        //if user logs out then send them back to the main menu
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    Intent intent = new Intent(CreateEditDelete.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };






    }
}
