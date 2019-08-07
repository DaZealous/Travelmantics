package model;

public class UserHelper {
    private String email;
    private String user_id;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserHelper(String email, String user_id, String name) {
        this.email = email;
        this.user_id = user_id;
        this.name = name;
    }

   public UserHelper(){

   }
}
