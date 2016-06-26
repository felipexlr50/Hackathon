package com.example.administrador.teste;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {


    private TextView CRMView;
    private EditText passwordView;
    private EditText CRMTxt;
    private View loginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        passwordView = (EditText) findViewById(R.id.passwordText);
        CRMTxt = (EditText) findViewById(R.id.txtCRM);




        loginFormView = findViewById(R.id.login_form);

    }




    public void send(View v){

    if(CRMTxt.getText().length()>0 && passwordView.length()>0) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.1.4/medical/app/api/autenticacao_mobile/login";



            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("post",response);

                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(json.getBoolean("sucesso")){
                            Intent intent = new Intent(LoginActivity.this,PacienteAcitivity.class);



                            intent.putExtra("id",json.getString("usuario"));
                            startActivity(intent);
                        }

                        else{
                            Toast.makeText(getBaseContext(),"Senha ou Email errado",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("email", CRMTxt.getText().toString());
                    MyData.put("senha",passwordView.getText().toString());
                    return MyData;
                }
            };
        MyRequestQueue.add(MyStringRequest);

    } else {

        Toast.makeText(getBaseContext(),"Campos vazios",Toast.LENGTH_SHORT).show();
    }
}

}



