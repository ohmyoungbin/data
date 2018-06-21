package com.example.gst.chatproj;

public class ChatData {

    private String userName;
    private String message;
    private String firebaseKey;
    private String userPotoUrl;
    private String userEmail;
    private long time;

    public ChatData() { }

    public ChatData(String userName, String message, String firebaseKey, String userPotoUrl, String userEmail, long time) {
        this.userName = userName;
        this.message = message;
        this.firebaseKey = firebaseKey;
        this.userPotoUrl = userPotoUrl;
        this.userEmail = userEmail;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public void setUserPotoUrl(String userPotoUrl) {
        this.userPotoUrl = userPotoUrl;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFirebaseKey() {

        return firebaseKey;
    }

    public String getUserPotoUrl() {
        return userPotoUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getTime() {
        return time;
    }
}
