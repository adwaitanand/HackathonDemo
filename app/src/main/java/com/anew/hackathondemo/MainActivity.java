package com.anew.hackathondemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    TextView att;
    EditText rollno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        att=(TextView)findViewById(R.id.Attendance);
        rollno=(EditText)findViewById(R.id.Rno);


//        Sheets sheetsService = null;
//        try {
//            sheetsService = createSheetsService();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        try {
//            Sheets.Spreadsheets.Values.Get request =
//                    sheetsService.spreadsheets().values().get("1vFDjWMYYhZaj-1IJoCWSi1xJnf9hXOxILwufGLS5zVY", "Sheet1");
//
//            ValueRange response = request.execute();
//            System.out.println(response);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void findAttendance(View view) {

        DownloadTask dt = new DownloadTask();
        String roll = rollno.getText().toString();
        if (roll.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter roll no", Toast.LENGTH_LONG).show();
            att.setText("");
        } else if (!isNetworkAvailable()) {
            //weather.setText("");
            Toast.makeText(getApplicationContext(), "could not find attendance check the internet connection", Toast.LENGTH_LONG).show();

        } else {
            // Log.i("city=", cityName.getText().toString());

            // InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
            try {
                // String msgenc = URLEncoder.encode(cityName.getText().toString(), "UTF-8");

                dt.execute("https://script.google.com/macros/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1vFDjWMYYhZaj-1IJoCWSi1xJnf9hXOxILwufGLS5zVY&sheet=Sheet1");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find attendance..check internet connection", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL ur;
            HttpURLConnection hur;
            try {
                ur = new URL(urls[0]);
                hur = (HttpURLConnection) ur.openConnection();
                InputStream in = hur.getInputStream();
                InputStreamReader inr = new InputStreamReader(in);
                int data = inr.read();
                while (data != -1) {
                    char c = (char) data;
                    result += c;
                    data = inr.read();
                }
                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find attendance", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try
            {
                JSONObject js=new JSONObject(s);
                String sheet=js.getString("Sheet1");
                Log.i("sheet1",sheet);
                JSONArray arr=new JSONArray(sheet);

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jpart=arr.getJSONObject(i);
                    String rno="";
                    String attend="";
                    rno=jpart.getString("Roll_No");
                    attend=jpart.getString("Attendance");
                    if(rno.equals(rollno.getText().toString()))
                    {
                        att.setText(attend+"%");
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        //        public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
//            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//            // TODO: Change placeholder below to generate authentication credentials. See
//            // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
//            //
//            // Authorize using one of the following scopes:
//            //   "https://www.googleapis.com/auth/drive"
//            //   "https://www.googleapis.com/auth/drive.file"
//            //   "https://www.googleapis.com/auth/drive.readonly"
//            //   "https://www.googleapis.com/auth/spreadsheets"
//            //   "https://www.googleapis.com/auth/spreadsheets.readonly"
//            GoogleCredential credential = null;
//
//            return new Sheets.Builder(httpTransport, jsonFactory, credential)
//                    .setApplicationName("Google-SheetsSample/0.1")
//                    .build(

    }

}
