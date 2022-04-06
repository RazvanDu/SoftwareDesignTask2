package com.RazvanDu.project.model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static HashMap<HttpSession, User> loggedUsers = new HashMap<>();
    public static HashMap<Long, List<Food>> cart = new HashMap<>();
    public static HashMap<Long, Integer> ordering = new HashMap<>();

}
