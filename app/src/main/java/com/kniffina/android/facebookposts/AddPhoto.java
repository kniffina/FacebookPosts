package com.kniffina.android.facebookposts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPhoto extends AppCompatActivity {

    private static int REQUEST_IMAGE_CAPTURE = 0;
    private static int PICK_IMAGE = 1;

    private ImageView imageView;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Button shareFromNewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Button shareFromPhone = (Button) findViewById(R.id.share_photo_from_phone);
        shareFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPictureGallery = new Intent();
                getPictureGallery.setType("image/*");
                getPictureGallery.setAction(Intent.ACTION_GET_CONTENT);

                if(getPictureGallery.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(getPictureGallery, PICK_IMAGE);
                }
            }
        });

        //set onclicklistener when user wants to take a photo from their phone and add to facebook
        shareFromNewPhoto = (Button) findViewById(R.id.take_photo_and_share);
        shareFromNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.v("Onclick after intent", "before if inside onclick");
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    Log.v("picture intent", "about to call startactivity");
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button postPhotoFacebook = (Button) findViewById(R.id.share_photo_to_facebook);
        postPhotoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) findViewById(R.id.photo_added_image_view);
                if(imageView.getDrawable() == null) {
                    String errorMessage = "You must first add a photo before posting to your Facebook";
                    Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                }
                else {
                    shareDialog = new ShareDialog(AddPhoto.this);
                    callbackManager = CallbackManager.Factory.create();
                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();

                    ShareContent shareContent = new ShareMediaContent.Builder()
                            .addMedium(photo)
                            .build();

                    shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
                    /*
                    NEED TO DOUBLE CHECK THAT A DIALOG APPEARS. THIS SAID THAT WE NEED TO HAVE NATIVE FACEBOOK INSTALLED
                     */

                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(getApplicationContext(),"Photo posted to your Facebook" , Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddPhoto.this, CreatePost.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancel() {
                            String errorMessage = "User cancelled posting";
                            Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.v("Error", error.toString());
                            String errorMessage = "You must have the native Facebook app installed to share photos";
                            Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        Button backtoShare = (Button) findViewById(R.id.share_photo_back_share);
        backtoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPhoto.this, CreatePost.class);
                startActivity(intent);
            }
        });

        Button backtoMain = (Button) findViewById(R.id.share_photo_back_main);
        backtoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPhoto.this, CreateEditDelete.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView = (ImageView) findViewById(R.id.photo_added_image_view);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Log.v("In onActivityResult", "Second if inside on activity");
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                imageView.setImageBitmap(imageBitmap);
            } else {
                String errorMessage = "Cancelled";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == PICK_IMAGE) {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imageView.setImageBitmap(image);
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open the image", Toast.LENGTH_LONG).show();
                }
            } else {
                String errorMessage = "Cancelled";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
