package com.example.spotifyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.example.spotifyexample.SpotifyModule;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "0602521b067c4083a4660fcc03f0eeaa";
    private static final String REDIRECT_URI = "com.example.spotifyexample://callback";
    private static final int REQUEST_CODE = 1337;
    Button btn, btn2;
    TextView textView;
    SpotifyModule sm = null;
    Boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        textView = (TextView)  findViewById(R.id.textView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected == false) {
                    con();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if ( sm != null){
                        //for some reason we have to do this in a thread

                   //https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               try {
                               String username = sm.getUserName();
                               textView.setText(username);
                               } catch (IOException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                       }).start();

               }
            }
        });

   }

    private void con(){
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();
        //do this only once xD
        AuthorizationClient.openLoginInBrowser(this, request);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthorizationResponse response = AuthorizationResponse.fromUri(uri);
            //System.out.println(response);
            //Log.v("reponse",response.toString());
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response

                    connected = true;
                    String tk = response.getAccessToken();
                    Toast.makeText(MainActivity.this, "Connected",
                            Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, tk,
                    //        Toast.LENGTH_SHORT).show();

                    sm = new SpotifyModule(tk, MainActivity.this);

                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(MainActivity.this, "ERROR",
                            Toast.LENGTH_LONG).show();
                    // Handle error response
                    break;


                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}

