package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PrivateConstants;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Class.forName;

public class LoginActivityWithAccessToken extends AppCompatActivity {

    private boolean valid;
    private EditText token;
    private SharedPreferences sharedPreferences;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity_with_access_token);

        if(!PrivateConstants.ACCESS_TOKEN.equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            if(sharedPreferences.contains("access_token")) {
                validateAccessToken(sharedPreferences.getString("access_token",""));
            }
        }

        valid =false;

        token = (EditText) findViewById(R.id.token);

        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccessToken(token.getText().toString());
            }
        });

    }

    private void validateAccessToken(final String token){

        final JsonArrayRequest validate = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.validateTokenURL(token), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                boolean errors = false;

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if(obj.has("errors")){
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_access_token), "Ikke gyldig aksessnøkkel", Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            EditText tokenText = (EditText) findViewById(R.id.token);

                            tokenText.setText("");

                            errors = true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(!errors){
                    valid = true;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", token);
                    editor.apply();

                    Intent intent = new Intent(getApplication(),MainActivity.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("login", "onErrorResponse: " + error.toString());
            }
        });

        RequestQueueHandler.getInstance(this).addToRequestQueue(validate);
    }

}
