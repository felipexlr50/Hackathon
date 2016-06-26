package com.example.administrador.teste;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class BDRequest extends Activity{

    private String onlineURL = "helloworldfatec.mybluemix.net/api";
    private String offlineURL = "http://192.168.1.4/medical/app/api/";
    private String ret="";

    public String connectWithHttpGet(String givenComand) {


        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String paramComand = params[0];
                System.out.println("paramLed is :" + paramComand);

                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(offlineURL
                        + paramComand);

                try {

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    System.out.println("httpResponse");

                    System.out.println("Status code: "
                            + httpResponse.getStatusLine().getStatusCode());

                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);

                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    System.out.println("Returning value of doInBackground :"
                            + stringBuilder.toString());

                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out
                            .println("Exception generates caz of httpResponse :"
                                    + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out
                            .println("Second exception generates caz of httpResponse :"
                                    + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                ret = result;


            }
        }

        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();

        httpGetAsyncTask.execute(givenComand);

        return ret;

    }



}
