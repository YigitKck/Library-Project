package com.semy.util;
import com.semy.entities.User;

// Login den aldığımız mail i tüm uygulama boyunca saklamak için (önemli!!)
public class SessionUser {
    private static SessionUser instance;
    private User user;
    private SessionUser() {}

    public static SessionUser getInstance() {
        if (instance == null) {
            instance = new SessionUser();
        }
        return instance;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }
}
