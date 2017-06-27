package com.kniffina.android.facebookposts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditPost extends AppCompatActivity {
    private ArrayList<FacebookData> fbData;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/posts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject res = response.getJSONObject();
                        try {
                            JSONArray array = res.getJSONArray("data");
                            fbData = new ArrayList<>();

                            for(int i = 0; i < array.length(); i++) {
                                try {
                                    String message = (String) array.getJSONObject(i).get("message");
                                    String date = (String) array.getJSONObject(i).get("created_time");
                                    String id = (String) array.getJSONObject(i).get("id");

                                    String formattedMessage;
                                    String formattedDate;
                                    try {
                                        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

                                        Date newDate = inputFormat.parse(date);
                                        formattedDate = outputFormat.format(newDate);
                                    } catch (ParseException pe) {
                                        System.out.println("Parse Exception : " + pe);
                                        formattedDate = "";
                                    }

                                    formattedMessage = message.trim();

                                    fbData.add(new FacebookData(formattedDate, formattedMessage, id)); //
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            FacebookAdapter fbAdapter = new FacebookAdapter(EditPost.this, fbData);

                            ListView listView = (ListView) findViewById(R.id.edit_list_view);
                            listView.setAdapter(fbAdapter);

                            listView = (ListView) findViewById(R.id.edit_list_view);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView textViewId = (TextView) view.findViewById(R.id.id_for_given_post);
                                    TextView textViewMessage = (TextView) view.findViewById(R.id.message_text_view);

                                    editConfirmation(textViewId.getText().toString(), textViewMessage.getText().toString());
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();


        //set up onclick listener to go back to main menu when clicked
        Button backMain = (Button) findViewById(R.id.edit_back_to_main_button);
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPost.this, CreateEditDelete.class);
                startActivity(intent);
            }
        });



    } //end on create

    //confirmation if the user wants to edit, the id of the post is sent to the function
    public void editConfirmation(String id, String editMessage) {
        final String postId = id;
        final EditText editText = new EditText(EditPost.this);
        editText.setText(editMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditPost.this);
        builder.setCancelable(true);
        builder.setTitle("Message to edit");
        builder.setView(editText);
        builder.setCancelable(true);
        builder.setPositiveButton("Save message",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String messaveToSave = editText.getText().toString();

                        Bundle params = new Bundle();
                        params.putString("message", messaveToSave);
                        //delete this post using the graph api
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                postId,
                                params,
                                HttpMethod.POST,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        Toast.makeText(getApplicationContext(),"Message has been saved for that Facebook post" , Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                        ).executeAsync();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //return to the activity
                Toast.makeText(getApplicationContext(),"Cancelled. Post has NOT been saved." , Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("in activityResult", "After call");
    }
}
