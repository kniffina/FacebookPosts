package com.kniffina.android.facebookposts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


public class AddUrl extends AppCompatActivity {

    private Button postFacebook;
    private EditText pasteUrl;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_url);

        pasteUrl = (EditText) findViewById(R.id.paste_url_edit_text);
        pasteUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasteUrl.setText("");
            }
        });

        postFacebook = (Button) findViewById(R.id.share_url_post_to_facebook);
        postFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the string in edit text and see if it is a valid url
                String pastedUrl = pasteUrl.getText().toString().trim();
                Log.v("pasted ur", ": " + pastedUrl);

                if(URLUtil.isValidUrl(pastedUrl) == true) {
                    shareDialog = new ShareDialog(AddUrl.this);
                    callbackManager = CallbackManager.Factory.create();

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(pastedUrl))
                            .build();

                    shareDialog.show(content);

                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(getApplicationContext(),"Posted to your Facebook" , Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddUrl.this, CreatePost.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancel() {
                            String errorMessage = "Cancelled";
                            Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(getApplicationContext(),error.toString() , Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else {
                    String errorMessage = "Not a valid URL. Check your URL and continue.";
                    Toast.makeText(getApplicationContext(),errorMessage , Toast.LENGTH_LONG).show();
                }
            }
        });

        Button backtoShare = (Button) findViewById(R.id.share_url_back_to_share);
        backtoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUrl.this, CreatePost.class);
                startActivity(intent);
            }
        });

        Button backtoMain = (Button) findViewById(R.id.share_url_back_to_main);
        backtoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUrl.this, CreateEditDelete.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
