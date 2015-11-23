package conectguitar.music.tcc.com.conectguitar.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
 * Created by lucas on 18/11/15.
 */
public class UpdateUser extends Activity {

    User user;
    DatabaseHelper helper = new DatabaseHelper(this);
    private ProgressDialog pDialog;

    //private static String url = "http://localhost:8000/users";
    //private static String urlt = "http://localhost:8000/teachers";

    EditText TFname, TFemail, TFuname, TFpass1, TFpass2, edidrelease;
    EditText edname, edemail, eduname, edpass1, edpass2;
    TextView txtName, txtEmail, txtUname;
    Button BUpdatebutton;
    Spinner spinner;

    String unameTeacher;
    String namestr;
    String emailstr;
    String unamestr;
    String pass1str;
    String pass2str;
    String temp;
    private String nome;
    int student_id;

    String id;
    public String name;
    public String email;
    public String uname;
    public String password;

    private int idt;
    int selectedItem;
    private int userType, userrelease=0, idteacher=0;

    boolean spinnerUserType=false;

    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_UNAME = "uname";
    private static final String TAG_PASS = "password";
    private static String url = "http://conectguitarws-conectguitar.rhcloud.com/users";
    //private static String urlt = "http://conectguitarws-conectguitar.rhcloud.com/teachers";

    JSONArray users = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> studentsList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateuser);

        Intent intent = getIntent();
        student_id = intent.getIntExtra("student_id", 0);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Wolfganger.otf");

        txtName = (TextView)findViewById(R.id.txtName);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtUname = (TextView)findViewById(R.id.txtUname);
        BUpdatebutton = (Button)findViewById(R.id.BUpdatebutton);

        new GetUsers().execute();

    }

    public void onUpdateUserClick(View v){

        if (v.getId() == R.id.BUpdatebutton)
        {

            edname = (EditText)findViewById(R.id.TFname);
            edemail = (EditText)findViewById(R.id.TFemail);
            eduname = (EditText)findViewById(R.id.TFuname);

            namestr = edname.getText().toString();
            emailstr = edemail.getText().toString();
            unamestr = eduname.getText().toString();


            if(namestr == null || namestr.equals("") || emailstr == null || emailstr.equals("") || emailstr == null || unamestr.equals("")) {
                Toast msg = Toast.makeText(UpdateUser.this , "Valor incorreto!!!" , Toast.LENGTH_SHORT);
                msg.show();
            }
            else {


                new UpdateUserAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users");
                //new UpdateUserAsyncTask().execute("http://localhost:8000/users");

                Toast salvo = Toast.makeText(UpdateUser.this, "Usuário atualizado!" , Toast.LENGTH_SHORT);
                salvo.show();


                clear((ViewGroup) findViewById(R.id.updatelayout));
            }



        }
    }

    public void clear(ViewGroup group) {

        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
        }
    }

    private class UpdateUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UpdateUser.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            user = new User();
            user.setName(namestr);
            user.setEmail(emailstr);
            user.setUname(unamestr);
            user.setPass(pass1str);

            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPut httpPUT;
                httpPUT = new HttpPut("http://conectguitarws-conectguitar.rhcloud.com/users/" + student_id);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", namestr);
                jsonObject.put("email", emailstr);
                jsonObject.put("uname", unamestr);
                jsonObject.put("password", pass1str);

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

    private class GetUsers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UpdateUser.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + "/" + student_id, ServiceHandler.GET);

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

                        id = c.getString(TAG_ID);
                        name = c.getString(TAG_NAME);
                        email = c.getString(TAG_EMAIL);
                        uname = c.getString(TAG_UNAME);
                        //password = c.getString(TAG_PASS);

                        //studentsList = new ArrayList<HashMap<String, String>>();

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_UNAME, uname);
                        //contact.put(TAG_PASS, password);



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

            //TFname.setText(name, TextView.BufferType.EDITABLE);
            txtName.setText("Nome: " + name);
            txtEmail.setText("Email: " + email);
            txtUname.setText("Usuário: " + uname);
            //TFpass1.setText(password);

            //ListAdapter adapter = new SimpleAdapter(Students.this, studentsList, R.layout.students_entry, new String[]{"id", "name", "release_id"}, new int[]{R.id.students_Id, R.id.students_name, R.id.students_idrelease});
            //lv.setAdapter(adapter);

        }

    }

}

