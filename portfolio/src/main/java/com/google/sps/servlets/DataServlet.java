// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.User;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  public static final int MIN_COMMENTS = 1;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get input from form
    String commentCountStr = request.getParameter("comment-count");
    int commentCount;
    try {
        commentCount = Integer.parseInt(commentCountStr);
    } catch (NumberFormatException e) {
        System.err.println("[ERROR] Parameter could not convert to int: " + commentCountStr);
        return;
    }

    if(commentCount < MIN_COMMENTS) {
      System.err.println("[ERROR] Number of comments to display lower than minimum comment constant of 1: " + commentCount);
    }

    // Count that keeps track of how many messages seen so far
    int count = 0;

    // Create a Query instance
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<Comment> comments = new ArrayList<>();
    
    for (Entity entity: results.asIterable()) {
        String message = (String) entity.getProperty("message");
        String userEmail = (String) entity.getProperty("userEmail");
        long id = entity.getKey().getId();
        long timestamp = (long) entity.getProperty("timestamp");

        Comment comment = new Comment(message, userEmail, id, timestamp);
        comments.add(comment);
        count++;
        if(count == commentCount) {
            break;
        }
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Servlet responsible for creating new comment tasks
    String message = request.getParameter("text-input");
    String userEmail = "undefined user";
    long timestamp = System.currentTimeMillis();
    UserService userService = UserServiceFactory.getUserService();
    
    if(userService.isUserLoggedIn()){
      userEmail = userService.getCurrentUser().getEmail();
    }
    // Create Entity of kind 'Comment'
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("message", message);
    commentEntity.setProperty("userEmail", userEmail);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }
}
