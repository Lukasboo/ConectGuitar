package conectguitar.music.tcc.com.conectguitar.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
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

import conectguitar.music.tcc.com.conectguitar.Controller.User;
import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;
import conectguitar.music.tcc.com.conectguitar.R;


public class MainActivity extends Activity {

    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_IDRELEASE = "release_id";
    private static final String TAG_IDTEACHER = "teacher_id";
    private static final String TAG_USERTYPE= "usertype";

    ArrayList<HashMap<String, String>> studentsList;


    DatabaseHelper helper = new DatabaseHelper(this);
    public int idrelease=2, idteacher=0;
    public String username;
    String emailstr;
    String passwordstr;
    static String validation;

    static String response = null;


    User user;
    private ProgressDialog pDialog;
    JSONArray users = null;
    Intent intent;

    Button BSignup;
    Button BLogin;
    EditText a;
    EditText b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");

        BSignup = (Button)findViewById(R.id.BSignup);
        BLogin = (Button)findViewById(R.id.BLogin);

        a = (EditText) findViewById(R.id.TFusername);
        b = (EditText) findViewById(R.id.TFpassword);

        BSignup.setTypeface(custom_font);
        BLogin.setTypeface(custom_font);
        a.setTypeface(custom_font);
        b.setTypeface(custom_font);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_users, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                //UpdateUser();
                return true;
            case R.id.item2:
                //DeleteUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void onButtonClick(View v){

        if(v.getId()==R.id.BLogin)

        {

            //EditText a = (EditText) findViewById(R.id.TFusername);
            emailstr = a.getText().toString();
            //EditText b = (EditText) findViewById(R.id.TFpassword);
            passwordstr = b.getText().toString();

            new UserValidateAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/login");
            //new UserValidateAsyncTask().execute("http://localhost:8000/users/login");

        }

        if(v.getId()==R.id.BSignup)

        {

            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);

        }
    }

    public static String POST(String url, User user){

        InputStream inputStream = null;
        HttpEntity httpEntity = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("password", user.getPass());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

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
            //httpEntity = httpResponse.getEntity();
            //response = EntityUtils.toString(httpEntity);

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

    private class UserValidateAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            user = new User();
            user.setEmail(emailstr);
            user.setPass(passwordstr);

            return POST(urls[0],user);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result == "") {
                Toast.makeText(getBaseContext(), "Usuário não existe!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Seja bem vindo!", Toast.LENGTH_LONG).show();


                try {
                    //JSONObject jsonObj;
                    JSONObject jsonObj;
                    jsonObj = new JSONObject(result);

                    // Getting JSON Array node
                    users = jsonObj.getJSONArray("users");

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        int id = c.getInt("id");
                        int student_id = c.getInt("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String uname = c.getString("uname");
                        int usertype = c.getInt("usertype");
                        int release_id = c.getInt("release_id");
                        int teacher_id = c.getInt("teacher_id");

                        if(usertype == 1) {

                            intent = new Intent(MainActivity.this, Display.class);
                            intent.putExtra("id", id);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("uname", uname);
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("idrelease", release_id);
                            intent.putExtra("idteacher", teacher_id);

                            startActivity(intent);
                        } else {

                            intent = new Intent(MainActivity.this, Stage.class);
                            intent.putExtra("id", id);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("uname", uname);
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("idrelease", release_id);
                            intent.putExtra("idteacher", teacher_id);

                            startActivity(intent);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

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



}
