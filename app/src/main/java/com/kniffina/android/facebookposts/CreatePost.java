package com.kniffina.android.facebookposts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.share.widget.ShareDialog;


public class CreatePost extends AppCompatActivity {
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //add onclick listener to back button to go to main page
        Button back = (Button) findViewById(R.id.create_back_main_menu_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, CreateEditDelete.class);
                startActivity(intent);
            }
        });

        Button urlButton = (Button) findViewById(R.id.create_share_timeline_url);
        urlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, AddUrl.class);
                startActivity(intent);
            }
        });

        Button photoButton = (Button) findViewById(R.id.create_share_timeline_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, AddPhoto.class);
                startActivity(intent);
            }
        });

        Button videoButton = (Button) findViewById(R.id.create_share_video_timeline);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, AddVideo.class);
                startActivity(intent);
            }
        });




    }
}