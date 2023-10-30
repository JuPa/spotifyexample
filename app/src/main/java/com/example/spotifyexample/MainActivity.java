package com.example.spotifyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "0602521b067c4083a4660fcc03f0eeaa";
    private static final String REDIRECT_URI = "com.example.spotifyexample://callback";
    private static final int REQUEST_CODE = 1337;

    Button btn;

    Boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (connected == false) {
                    con();
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
            System.out.println(response);
            Log.v("reponse",response.toString());
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Toast.makeText(MainActivity.this, "connected",
                            Toast.LENGTH_LONG).show();
                    connected = true;

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
    /* void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Toast.makeText(MainActivity.this, "TOKEn",
                            Toast.LENGTH_LONG).show();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(MainActivity.this, "ERROR",
                            Toast.LENGTH_LONG).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(MainActivity.this, "default",
                            Toast.LENGTH_LONG).show();
                    // Handle other cases
            }*/
        }
    }
}

