package conectguitar.music.tcc.com.conectguitar.Controller;

/**
 * Created by lucas on 21/11/15.
 */
public class Feedback {

    int id, student_id;
    String message, original_filename, created_at, updated_at;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_ORIGINAL_FILENAME = "original_filename";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public Feedback() {

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getStudent_Id() {
        return student_id;
    }

    public void setStudent_Id(int student_id) {
        this.student_id = student_id;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginal_Filename() { return original_filename; }

    public void setOriginal_Filename(String original_filename) {
        this.original_filename = original_filename;
    }

    public String getCreated_at() { return created_at; }

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at; }

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}






}

