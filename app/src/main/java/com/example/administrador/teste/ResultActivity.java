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
import com.jjoe64.graphview.series.Series;

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


    private Viewport viewTemp;
    private Viewport viewUmidade;



    private LineGraphSeries<DataPoint> seriesTempIn;
    private LineGraphSeries<DataPoint> seriesTempEx;
    private LineGraphSeries<DataPoint> seriesUmidade;


    private GraphView graphTemp;
    private GraphView graphUmidade;

    private TextView nome;
    private TextView sobrenome;
    private TextView idade;
    private TextView CID;
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
        sobrenome = (TextView) findViewById(R.id.textView4);
        idade = (TextView) findViewById(R.id.textView5);
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
                        Log.d("prontuario2",response);
                        try {


                            JSONObject json = new JSONObject(response);
                            JSONArray jarray = json.getJSONArray("prontuario");
                            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject oneObject = jarray.getJSONObject(i);
                               nome2 = oneObject.getString("nome");
                                Log.i("testeJason",oneObject.getString("nome"));
                                sobrenome2 = oneObject.getString("sobrenome");
                                Log.i("testeVariavel",nome2);
                              idade2 = df2.parse(oneObject.getString("datahora")).toString();
                               CID2 = oneObject.getString("diagnostico");


                                nome.setText(nome2);
                                sobrenome.setText(sobrenome2);
                                idade.setText(idade2);
                                CID.setText(CID2);

                            }


                        } catch (JSONException e) {

                        } catch (ParseException e) {
                            e.printStackTrace();
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
        dadosGrafico("http://192.168.1.4/medical/app/api/prontuario_mobile/getProntuario/"+idProntuarios);
        graphTemp.setTitle("Grafico de Temperatura");
        graphTemp.getGridLabelRenderer().setHorizontalAxisTitle("Tempo horas");
        graphTemp.getGridLabelRenderer().setVerticalAxisTitle("Temperatura Celcius");
        seriesTempEx.setColor(Color.RED);
        seriesTempIn.setTitle("Temp Paciente");
        seriesTempIn.setColor(Color.BLUE);
        seriesTempEx.setTitle("Temp Ambiente");
        graphTemp.getLegendRenderer().setVisible(true);
        graphTemp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphTemp.getGridLabelRenderer().setNumHorizontalLabels(3);


        viewTemp = graphTemp.getViewport();
        viewTemp.setYAxisBoundsManual(true);
        viewTemp.setMaxY(50);

        viewTemp.setMinY(0);




    }

    private void mostrarUmidadeGraph(){
        graphUmidade = (GraphView) findViewById(R.id.graphUmidade);
        seriesUmidade = new LineGraphSeries<DataPoint>();
        graphUmidade.addSeries(seriesUmidade);
        dadosGrafico2("http://192.168.1.4/medical/app/api/prontuario_mobile/getProntuario/"+idProntuarios);
        graphUmidade.setTitle("Grafico de Temperatura");
        graphUmidade.getGridLabelRenderer().setHorizontalAxisTitle("Tempo horas");
        graphUmidade.getGridLabelRenderer().setVerticalAxisTitle("Umidade (%)");

        graphUmidade.getGridLabelRenderer().setNumHorizontalLabels(5);



        viewUmidade = graphUmidade.getViewport();
        viewUmidade.setYAxisBoundsManual(true);
        viewUmidade.setMaxY(100);


        viewUmidade.setMinY(0);



    }


    public void dadosGrafico(String url){


        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject json = new JSONObject(response);
                            JSONArray jarray = json.getJSONArray("sensores");
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject oneObject = jarray.getJSONObject(i);
                                double tempIn = oneObject.getDouble("temperatura");
                                double tempOut = oneObject.getDouble("temperatura_externa");
                                Date hora = df.parse(oneObject.getString("datahora"));
                                seriesTempIn.appendData(new DataPoint(hora, tempIn),true,10);
                                seriesTempEx.appendData(new DataPoint(hora,tempOut),true,10);


                            }


                        } catch (JSONException e) {

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



    }


    public void dadosGrafico2(String url){


        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject json = new JSONObject(response);
                            JSONArray jarray = json.getJSONArray("sensores");
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject oneObject = jarray.getJSONObject(i);
                                double tempIn = oneObject.getDouble("umidade");

                                Date hora = df.parse(oneObject.getString("datahora"));
                                seriesUmidade.appendData(new DataPoint(hora, tempIn),true,10);



                            }


                        } catch (JSONException e) {

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



    }



}
