package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
public class SignUp extends Activity {

    User user;
    DatabaseHelper helper = new DatabaseHelper(this);
    private ProgressDialog pDialog;

    //private static String url = "http://localhost:8000/users";
    //private static String urlt = "http://localhost:8000/teachers";

    EditText TFname, TFemail, TFuname, TFpass1, TFpass2, edidrelease;
    TextView txt1, txt2, txt3, txt4, txt5;
    Button SignUp;
    Spinner spinner;

    String unameTeacher;
    String namestr;
    String emailstr;
    String unamestr;
    String pass1str;
    String pass2str;
    String userTypestr;
    String userreleasestr;
    String idteacherstr;
    String temp;
    private String nome;

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
    private static final String TAG_USERTYPE = "usertype";
    private static final String TAG_IDRELEASE  = "release_id";
    private static final String TAG_IDTEACHER = "teacher_id";
    private static String url = "http://conectguitarws-conectguitar.rhcloud.com/users";
    private static String urlt = "http://conectguitarws-conectguitar.rhcloud.com/teachers";

    JSONArray users = null;
    ArrayList<HashMap<String, String>> studentsList;
    private ArrayList<User> teachersList;
    HashMap<String, String> teclist;

    EditText name;
    EditText email;
    EditText uname;
    EditText pass1;
    EditText pass2;
    //Button SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        CheckBox teacher = (CheckBox)findViewById(R.id.chkTeacher);
        spinner=(Spinner)findViewById(R.id.spinner);
        teachersList = new ArrayList<User>();
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");

        name = (EditText)findViewById(R.id.TFname);
        email = (EditText)findViewById(R.id.TFemail);
        uname = (EditText)findViewById(R.id.TFuname);
        pass1 = (EditText)findViewById(R.id.TFpass1);
        pass2 = (EditText)findViewById(R.id.TFpass2);
        SignUp = (Button)findViewById(R.id.Bsignupbutton);

        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3 = (TextView)findViewById(R.id.txt3);
        txt4 = (TextView)findViewById(R.id.txt4);
        txt5 = (TextView)findViewById(R.id.txt5);

        //SignUp = (Button)findViewById(R.id.BSignup);

        name.setTypeface(custom_font);
        email.setTypeface(custom_font);
        uname.setTypeface(custom_font);
        pass1.setTypeface(custom_font);
        pass2.setTypeface(custom_font);
        txt1.setTypeface(custom_font);
        txt2.setTypeface(custom_font);
        txt3.setTypeface(custom_font);
        txt4.setTypeface(custom_font);
        txt5.setTypeface(custom_font);
        teacher.setTypeface(custom_font);
        SignUp.setTypeface(custom_font);
        //sspinner.setTypeface(custom_font);
        new GetTeachers().execute();

    }

    public void onSignUpClick(View v){

        if (v.getId() == R.id.Bsignupbutton)
        {

            /*EditText name = (EditText)findViewById(R.id.TFname);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText uname = (EditText)findViewById(R.id.TFuname);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);*/

            /*EditText name = (EditText)findViewById(R.id.TFname);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText uname = (EditText)findViewById(R.id.TFuname);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);*/

            namestr = name.getText().toString();
            emailstr = email.getText().toString();
            unamestr = uname.getText().toString();
            pass1str = pass1.getText().toString();
            pass2str = pass2.getText().toString();

            if(userType==1){
                userrelease = 30;
            } else {
                userrelease = 2;

                temp = (String) spinner.getSelectedItem();
                idteacher = Integer.parseInt(teclist.get(temp));

            }

            if(!pass1str.equals(pass2str)){

                Toast pass = Toast.makeText(SignUp.this , "Password dont match" , Toast.LENGTH_SHORT);
                pass.show();

            }
            else{

                if(namestr == null || namestr.equals("") || emailstr == null || emailstr.equals("") || emailstr == null || unamestr.equals("") || pass1str.equals("") || pass2str.equals("")) {
                    Toast msg = Toast.makeText(SignUp.this , "Incorrect Value!!!" , Toast.LENGTH_SHORT);
                    msg.show();
                }
                else {

                    userTypestr = String.valueOf(userType);
                    userreleasestr = String.valueOf(userrelease);
                    idteacherstr = String.valueOf(idteacher);

                    new InsertUserAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users");
                    //new InsertUserAsyncTask().execute("http://localhost:8000/users");

                    Toast salvo = Toast.makeText(SignUp.this, "Welcome " + namestr + "!" , Toast.LENGTH_SHORT);
                    salvo.show();


                    clear((ViewGroup) findViewById(R.id.signuplayout));
                }

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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.chkTeacher:
                if (checked) {
                    userType = 1;
                    idteacher = 0;
                    spinner.setEnabled(false);
                } else {
                    userType = 0;
                    idteacher = 0;
                    spinner.setEnabled(true);
                }
                break;
        }
    }

    public String teacherSpinner() {

        String unameTeacher = spinner.getSelectedItem().toString();
        return unameTeacher;
    }

    public static String POST(String url, User user){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", user.getName());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("uname", user.getUname());
            jsonObject.accumulate("password", user.getPass());
            jsonObject.accumulate("usertype", user.getUserType());
            jsonObject.accumulate("release_id", user.getIdRelease());
            jsonObject.accumulate("teacher_id", user.getIdTeacher());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

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

        // 11. return result
        return result;
    }

    private class InsertUserAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            user = new User();
            user.setName(namestr);
            user.setEmail(emailstr);
            user.setUname(unamestr);
            user.setPass(pass1str);
            user.setUserType(userType);
            user.setIdRelease(userrelease);
            user.setIdTeacher(idteacher);


            return POST(urls[0],user);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class GetTeachers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlt, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj != null) {
                        JSONArray teachers = jsonObj
                                .getJSONArray(TAG_USERS);

                        for (int i = 0; i < teachers.length(); i++) {
                            JSONObject userObj = (JSONObject) teachers.get(i);
                            User teacher = new User(userObj.getInt("id"), userObj.getString("name"));

                            teachersList.add(teacher);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
            //return teachersList;
        }

        @Override
        //protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            populateSpinner();

        }

    }

    private void populateSpinner() {

        String[] lables = new String[teachersList.size()];
        HashMap<String, String> spinnerMap = new HashMap<String, String>();

        teclist = new HashMap<String, String>();
        for (int i = 0; i < teachersList.size(); i++) {
            //teclist.put("id", String.valueOf(teachersList.get(i).getId()));
            teclist.put(String.valueOf(teachersList.get(i).getName()), String.valueOf(teachersList.get(i).getId()));
            lables[i] = teachersList.get(i).getName();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);

    }

}

