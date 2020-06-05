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

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    List<String> messages;
    messages = new ArrayList<String>();
    messages.add("Hello World");
    messages.add("Goodbye World");
    messages.add("Hello Goodbye");

    String msgLstJson = convertToJsonUsingGson(messages);
    response.setContentType("applications/json;");
    response.getWriter().println(messages);

    // Create a Query instance
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    
    for (Entity entity: results.asIterable()) {
        String msg = (String) entity.getProperty("msg");
        long id = entity.getKey().getId();
        long timestamp = (long) entity.getProperty("timestamp");

        Comment comment = new Comment(msg, id, timestamp);
        comments.add(comment);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Respond with the result.
    String comment = request.getParameter("text-input");
    response.setContentType("data/html;");
    response.getWriter().println(comment);

    // Servlet responsible for creating new comment tasks
    String msg = request.getParameter("text-input");
    long timestamp = System.currentTimeMillis();

    // Create Entity of kind 'Comment'
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("msg", msg);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

 /**
   * Converts a list of messages into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(List<String> arr) {
    Gson gson = new Gson();
    String json = gson.toJson(arr);
    return json;
  }

}
