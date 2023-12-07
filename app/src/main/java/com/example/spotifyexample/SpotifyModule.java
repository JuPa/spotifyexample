package com.example.spotifyexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SpotifyModule  {
    Context context;
    public SpotifyModule(String bearerToken, Context context){
        //TODO save this tk in SharedPreferences
        this.context = context;
        if (bearerToken.length() > 0) {
            saveKey(bearerToken);
        }

    }

    public String getUserName() throws IOException {
        String userName = null;
        String token = getKey();
        if (token != null && token.length() > 0) { //double check if bearer is existing

            URL spotifyEndpoint = new URL("https://api.spotify.com/v1/me");
                // Create connection
            HttpsURLConnection myConnection =
                    (HttpsURLConnection) spotifyEndpoint.openConnection();
            //adding headers....
            myConnection.setRequestProperty("Authorization", "Bearer " + token);
            myConnection.setRequestMethod("GET");

            if (myConnection.getResponseCode() == 200) {
                InputStream in = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(in, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                //parse with json reader (wtf is this shit)
                userName=  parseJsonKey(jsonReader,"display_name");

            } else if (myConnection.getResponseCode() == 401) {
                Toast.makeText(context, "Bad or expired token",
                        Toast.LENGTH_LONG).show();
            }
            else if (myConnection.getResponseCode() == 403) {
                Toast.makeText(context, "Bad OAuth request",
                        Toast.LENGTH_LONG).show();
            }
            else if (myConnection.getResponseCode() == 429) {
                Toast.makeText(context, "The app has exceeded its rate limits.",
                        Toast.LENGTH_LONG).show();
            }
            //at all cases close
            myConnection.disconnect();
        }
        return userName;
    }




    private Boolean saveKey(String key){
        //Resource.saveData("weight" , Double.toString(dataWeight),"personalData", getActivity().getApplicationContext());
        SharedPreferences savedStr = context.getApplicationContext().getSharedPreferences("keyStore", 0);
        SharedPreferences.Editor editor = savedStr.edit();
        editor.putString("userKey", key);
        editor.apply();
        return true;
    }

    private String getKey(){
        SharedPreferences savedData = context.getSharedPreferences("keyStore", 0); //lädt SharedPreference mit gegebenen Namen
        return savedData.getString("userKey", null);                    //lädt Wert aus Variable mit gegebenen namen
    }

    private String parseJsonKey(JsonReader jsonReader ,String key) throws IOException {
        jsonReader.beginObject(); // Start processing the JSON object 
        while (jsonReader.hasNext()) { // Loop through all keys 
            String jsonKey = jsonReader.nextName(); // Fetch the next key 
            if (jsonKey.equals(key)) { // Check if desired key 
                // Fetch the value as a String 
                String value = jsonReader.nextString();
                return value;
                //break; // Break out of the loop
            } else {
                jsonReader.skipValue(); // Skip values of other keys
            }

        }
        return null;

    }
}
