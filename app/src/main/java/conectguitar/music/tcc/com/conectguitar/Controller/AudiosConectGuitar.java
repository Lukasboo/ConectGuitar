package conectguitar.music.tcc.com.conectguitar.Controller;

/**
 * Created by lucas on 06/10/15.
 */
public class AudiosConectGuitar {

    int id, student_id, stage, lesson;
    String filename, mime, original_filename;
    //Date created_at, updated_at;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STAGE = "stage";
    public static final String COLUMN_LESSON = "lesson";
    public static final String COLUMN_FILENAME = "filename";
    //public static final String COLUMN_MIME = "mime";
    public static final String COLUMN_ORIGINAL_FILENAME = "original_filename";
    /*public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATE_AT = "update_at";*/

    /*public AudiosConectGuitar(int id, String name) {

        this.id = id;
        this.name = name;

    }*/

    public AudiosConectGuitar() {

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getStudent_Id() {
        return student_id;
    }

    public void setStudent_Id(int student_id) {
        this.student_id = student_id;
    }

    public int getStage() { return stage; }

    public void setStage(int stage) { this.stage = stage;}

    public int getLesson() { return lesson; }

    public void setLesson(int lesson) { this.lesson = lesson;}

    public String getFilename() { return filename; }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    //public String getMime() { return mime; }

    //public void setMime(String mime) { this.mime = mime;}

    public String getOriginal_Filename() { return original_filename; }

    public void setOriginal_Filename(String original_filename) { this.original_filename = original_filename;}

    //public Date getCreated_At() { return created_at; }

    //public void setCreated_At(Date created_at) { this.created_at = created_at;}

    //public Date getUpdated_At() { return updated_at; }

    //public void setUdated_At(Date updated_at) { this.updated_at = updated_at;}




}

