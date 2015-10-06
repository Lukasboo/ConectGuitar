package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Controller.ServiceHandler;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class Students extends Activity {

    private ProgressDialog pDialog;

    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_IDRELEASE = "release_id";


    TextView students_Id, students_Name, students_IdRelease;
    private int idteacher=0;
    private int studentsIdRelease=0;
    int id;
    private static String url = "http://conectguitarws-conectguitar.rhcloud.com/teachers/";
    // users JSONArray
    JSONArray users = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> studentsList;
    ListView lv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students);
        //int idteacher=0;
        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        idteacher = globalVar.getInt("idteacher", 0);*/
        Intent intent = getIntent();
        idteacher = intent.getIntExtra("idteacher", 0);
        id = intent.getIntExtra("id", 0);
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                students_Id = (TextView) view.findViewById(R.id.students_Id);
                students_Name = (TextView) view.findViewById(R.id.students_name);
                students_IdRelease = (TextView) view.findViewById(R.id.students_idrelease);
                String studentsId = students_Id.getText().toString();
                String studentsName = students_Name.getText().toString();
                String studentsIdRelease = students_IdRelease.getText().toString();
                Intent objIndent = new Intent(getApplicationContext(), StudentsRelease.class);
                objIndent.putExtra("students_Id", Integer.parseInt(studentsId));
                objIndent.putExtra("students_Name", studentsName);
                objIndent.putExtra("students_IdRelease", Integer.parseInt(studentsIdRelease));
                startActivity(objIndent);
            }
        });

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Students.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + id, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    users = jsonObj.getJSONArray(TAG_USERS);
                    studentsList = new ArrayList<HashMap<String, String>>();
                    // looping through All Contacts
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String release_id = c.getString(TAG_IDRELEASE);
                        //studentsList = new ArrayList<HashMap<String, String>>();

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_IDRELEASE, release_id);

                        // adding contact to contact list
                        studentsList.add(contact);
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

            ListAdapter adapter = new SimpleAdapter(Students.this, studentsList, R.layout.students_entry, new String[]{"id", "name", "release_id"}, new int[]{R.id.students_Id, R.id.students_name, R.id.students_idrelease});
            lv.setAdapter(adapter);

        }

    }

}

