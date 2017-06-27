package com.kniffina.android.facebookposts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.lang.reflect.Field;

public class AddVideo extends AppCompatActivity {
    private static int REQUEST_VIDEO_CAPTURE = 0;
    private static int PICK_VIDEO = 1;

    private VideoView videoView;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        Button addNewVideo = (Button) findViewById(R.id.add_video_new_video_create);
        addNewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);

                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "inside of ELSE", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button addFromPhone = (Button) findViewById(R.id.take_video_with_phone);
        addFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getVideoGallery = new Intent();
                getVideoGallery.setType("video/*");
                getVideoGallery.setAction(Intent.ACTION_GET_CONTENT);

                if(getVideoGallery.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(getVideoGallery, PICK_VIDEO);
                }
            }
        });



        Button backtoShare = (Button) findViewById(R.id.share_video_back_share);
        backtoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddVideo.this, CreatePost.class);
                startActivity(intent);
            }
        });

        Button backtoMain = (Button) findViewById(R.id.share_video_back_main);
        backtoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddVideo.this, CreateEditDelete.class);
                startActivity(intent);
            }
        });

        Button postToFacebook = (Button) findViewById(R.id.share_video_to_facebook);
        postToFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView = (VideoView) findViewById(R.id.video_view);

                if(videoView.getDrawableState() == null) {
                    Toast.makeText(getApplicationContext(), "There is no video uploaded. Please upload a video and retry", Toast.LENGTH_LONG).show();
                }
                else {
                    shareDialog = new ShareDialog(AddVideo.this);
                    callbackManager = CallbackManager.Factory.create();

                    Uri mUri = null;
                    try {
                        Field mUriField = VideoView.class.getDeclaredField("mUri");
                        mUriField.setAccessible(true);
                        mUri = (Uri)mUriField.get(videoView);

                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(mUri)
                            .build();

                    ShareVideoContent shareVideoContent = new ShareVideoContent.Builder()
                            .setVideo(video)
                            .build();

                    shareDialog.show(shareVideoContent, ShareDialog.Mode.AUTOMATIC);
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(getApplicationContext(), "Video posted to your Facebook", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(AddVideo.this, CreatePost.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getApplicationContext(),"User cancelled posting" , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            String errorMessage = "You either need to upload a video, or you do not have the Native Facebook app installed.";
                            Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        videoView = (VideoView) findViewById(R.id.video_view);

        if(requestCode == REQUEST_VIDEO_CAPTURE) {
            if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "in request video capture", Toast.LENGTH_LONG).show();
                videoUri = data.getData();
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
            else {
                String errorMessage = "Cancelled";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }

        else if(requestCode == PICK_VIDEO) {
            Toast.makeText(getApplicationContext(), "Pick Video", Toast.LENGTH_LONG).show();
            if(requestCode == PICK_VIDEO && resultCode == RESULT_OK) {
                videoUri = data.getData();
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
            else {
                String errorMessage = "Cancelled";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }
}
