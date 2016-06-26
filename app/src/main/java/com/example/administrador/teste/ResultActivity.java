package com.example.administrador.teste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    String idMedico;
    String idProntuarios;

     TextView nome;
    private Viewport viewTemp;
    private Viewport viewUmidade;



    private LineGraphSeries<DataPoint> seriesTempIn;
    private LineGraphSeries<DataPoint> seriesTempEx;
    private LineGraphSeries<DataPoint> seriesUmidade;


    private GraphView graphTemp;
    private GraphView graphUmidade;


     TextView sobrenome;
     TextView idade;
     TextView CID;
    String nome2, sobrenome2,idade2,CID2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String id;

        Intent intent = getIntent();

        idMedico = intent.getStringExtra("idM");
        idProntuarios = intent.getStringExtra("id");


        Intent getValue = getIntent();
        nome = (TextView) findViewById(R.id.textView4);
        sobrenome = (TextView) findViewById(R.id.textView5);
        idade = (TextView) findViewById(R.id.textView6);
        CID = (TextView) findViewById(R.id.textView8);





        graphUmidade = (GraphView) findViewById(R.id.graphUmidade);


        mostrarTempGraph();
        mostrarUmidadeGraph();

        pegarDados("http://192.168.1.4/medical/app/api/prontuario_mobile/getProntuario/"+idProntuarios);

        nome.setText(nome2);
        sobrenome.setText(sobrenome2);
        idade.setText(idade2);
        CID.setText(CID2);



    }

    private void pegarDados(String url){
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reaponse",response);
                        try {
                            Date data = new Date();

                            JSONObject json = new JSONObject(response);
                            JSONArray jarray = json.getJSONArray("prontuario");

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject oneObject = jarray.getJSONObject(i);
                               nome2 = oneObject.getString("nome");
                                sobrenome2 = oneObject.getString("sobrenome");
                               idade2 = oneObject.getString("datahora");
                               CID2 = oneObject.getString("diagnostico");

                            }


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



    private void mostrarTempGraph(){
        graphTemp = (GraphView) findViewById(R.id.graphTemp);
        seriesTempIn = new LineGraphSeries<DataPoint>();
        seriesTempEx = new LineGraphSeries<DataPoint>();
        graphTemp.addSeries(seriesTempIn);
        graphTemp.addSeries(seriesTempEx);
        graphTemp.setTitle("Grafico de Temperatura");
        graphTemp.getGridLabelRenderer().setHorizontalAxisTitle("Tempo horas");
        graphTemp.getGridLabelRenderer().setVerticalAxisTitle("Temperatura Celcius");
        seriesTempEx.setColor(Color.RED);
        seriesTempIn.setTitle("Temp Paciente");
        seriesTempIn.setColor(Color.BLUE);
        seriesTempEx.setTitle("Temp Ambiente");
        graphTemp.getLegendRenderer().setVisible(true);
        graphTemp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        for(int i = 1;i<=24;i++){
            seriesTempIn.appendData(new DataPoint(i,i+new Random().nextInt(40)),true,24);
            seriesTempEx.appendData(new DataPoint(i,i+new Random().nextInt(50)),true,24);
        }

        viewTemp = graphTemp.getViewport();
        viewTemp.setYAxisBoundsManual(true);
        viewTemp.setMaxY(50);
        viewTemp.setMinX(0);
        viewTemp.setMinY(0);
        viewTemp.setMaxX(24);

    }

    private void mostrarUmidadeGraph(){
        graphUmidade = (GraphView) findViewById(R.id.graphUmidade);
        seriesUmidade = new LineGraphSeries<DataPoint>();
        graphUmidade.addSeries(seriesUmidade);
        graphUmidade.setTitle("Grafico de Temperatura");
        graphUmidade.getGridLabelRenderer().setHorizontalAxisTitle("Tempo horas");
        graphUmidade.getGridLabelRenderer().setVerticalAxisTitle("Umidade (%)");



        for(int i = 1;i<=24;i++){
            seriesUmidade.appendData(new DataPoint(i,i+new Random().nextInt(100)),true,24);
        }

        viewUmidade = graphUmidade.getViewport();
        viewUmidade.setYAxisBoundsManual(true);
        viewUmidade.setMaxY(100);
        viewUmidade.setMinX(0);
        viewUmidade.setMinY(0);
        viewUmidade.setMaxX(24);

    }



}
