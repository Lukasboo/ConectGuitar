package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Controller.ServiceHandler;
import conectguitar.music.tcc.com.conectguitar.Controller.User;
import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class StudentsRelease extends Activity {

    User user;
    ListView lv;
    EditText edStudent, edRelease;
    Spinner spinner;
    Button btnSave;
    private ProgressDialog pDialog;
    String url;
    DatabaseHelper helper = new DatabaseHelper(this);

    private int students_Id=0, students_IdRelease=0, idrelease=0, newIdRelease=0;
    private String students_Name, descriptionRelease;

    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_ORIGINAL_FILENAME = "original_filename";

    JSONArray lessons = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_release);

        edStudent = (EditText)findViewById(R.id.edStudent);
        edRelease = (EditText)findViewById(R.id.edRelease);
        //spinner = (Spinner)findViewById(R.id.spinner);
        btnSave = (Button)findViewById(R.id.btnSave);

        Intent intent = getIntent();
        students_Id = intent.getIntExtra("students_Id", 0);
        students_Name = intent.getStringExtra("students_Name");
        students_IdRelease = intent.getIntExtra("students_IdRelease", 0);

        edStudent.setText("Aluno:" + students_Name);

        edRelease.setText(helper.getStudentReleases(students_IdRelease));

        helper.getReleases();
        ArrayList<String> listReleases=helper.getReleases();
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner, R.id.txtSpinner, listReleases);
        spinner.setAdapter(adapter);

        url = "http://conectguitarws-conectguitar.rhcloud.com/fileentry/";

        lv = (ListView) findViewById(R.id.lessonList);
        new GetLessons().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public String releaseSpinner() {

        String descriptionRelease = spinner.getSelectedItem().toString();
        return descriptionRelease;
    }

    public void onClick(View view) {

        descriptionRelease = releaseSpinner();
        idrelease = helper.searchIdRelease(descriptionRelease);
        newIdRelease = helper.searchIdRelease(descriptionRelease);

        User c = new User();
        c.setId(students_Id);
        c.setIdRelease(newIdRelease);

        new HttpAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/" + students_Id);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(StudentsRelease.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            user = new User();
            user.setIdRelease(newIdRelease);

            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPut httpPUT;
                httpPUT = new HttpPut("http://conectguitarws-conectguitar.rhcloud.com/teachers/" + students_Id);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("release_id", newIdRelease);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPUT.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPUT.setHeader("Accept", "application/json");
                httpPUT.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPUT);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return "EXITO!";


        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();

        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class GetLessons extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(StudentsRelease.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + students_Id, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    lessons = jsonObj.getJSONArray(TAG_USERS);
                    lessonList = new ArrayList<HashMap<String, String>>();
                    // looping through All Contacts
                    for (int i = 0; i < lessons.length(); i++) {
                        JSONObject c = lessons.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String original_filename = c.getString(TAG_ORIGINAL_FILENAME);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_ORIGINAL_FILENAME, original_filename);
                        //contact.put(TAG_IDRELEASE, release_id);

                        // adding contact to contact list
                        lessonList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(StudentsRelease.this, lessonList, R.layout.lessons_entry, new String[]{"id", "original_filename"}, new int[]{R.id.lessons_Id, R.id.lessons_original_filename});
            lv.setAdapter(adapter);

        }

    }


}

