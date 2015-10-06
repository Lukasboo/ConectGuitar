package conectguitar.music.tcc.com.conectguitar.Controller;

/**
 * Created by lucas on 06/10/15.
 */
public class User {

    int id, usertype, idrelease, idteacher;
    String name, email, uname, pass;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_UNAME = "uname";
    public static final String COLUMN_PASS = "pass";
    public static final String COLUMN_USERTYPE = "usertype";
    public static final String COLUMN_IDRELEASE = "idrelease";
    public static final String COLUMN_IDTEACHER = "idteacher";

    public User(int id, String name) {

        this.id = id;
        this.name = name;

    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getUserType() { return usertype; }

    public void setUserType(int usertype) { this.usertype = usertype;}

    public int getIdRelease() { return idrelease; }

    public void setIdRelease(int idrelease) { this.idrelease = idrelease;}

    public int getIdTeacher() { return idteacher; }

    public void setIdTeacher(int idteacher) { this.idteacher = idteacher;}



}

