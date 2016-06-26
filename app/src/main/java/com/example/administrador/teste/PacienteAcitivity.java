package com.example.administrador.teste;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PacienteAcitivity extends ListActivity {

        String[] arrayOfString;
        int[] idArray;
        ArrayList<String> aux2;
        String idMedico;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        idMedico = intent.getStringExtra("id");
        listarPacientes("http://192.168.1.4/medical/app/api/prontuario_mobile/listar/"+intent.getStringExtra("id"));



    }

    private void listarPacientes(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reaponse",response);
                        try {

                            JSONObject json = new JSONObject(response);
                            JSONArray jarray = json.getJSONArray("prontuarios");
                            idArray = new int[jarray.length()];
                            arrayOfString = new String[jarray.length()];
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject oneObject = jarray.getJSONObject(i);
                                arrayOfString[i] = oneObject.getString("nome");
                                idArray[i] = oneObject.getInt("id");
                            }

                            makeList(arrayOfString);
                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);


    }


    private void makeList(String[] arrayOfString) {



            if (arrayOfString.length == 0) {
                arrayOfString = new String[]{"Sem usuario!"};

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.layout_listview, arrayOfString);
                setListAdapter(adapter);
                registerForContextMenu(getListView());

            } else {

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.layout_listview, arrayOfString);

                setListAdapter(adapter);

                registerForContextMenu(getListView());

                 ListView listView = getListView();
                 aux2 = new ArrayList<String>();

                for(int i = 0;i<arrayOfString.length;i++){

                    aux2.add(arrayOfString[i]);

                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedFromList = (String) getListView().getItemAtPosition(i);
                        String id = String.valueOf(idArray[aux2.indexOf(selectedFromList)]);

                        Intent openPacientInfo = new Intent(PacienteAcitivity.this, ResultActivity.class);
                        openPacientInfo.putExtra("id",id);
                        openPacientInfo.putExtra("idM",idMedico);
                        startActivity(openPacientInfo);
                    }
                });
            }



    }


}
