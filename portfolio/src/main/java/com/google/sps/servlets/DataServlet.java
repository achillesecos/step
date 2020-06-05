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
import com.google.gson.Gson;

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
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Respond with the result.
    String comment = request.getParameter("text-input");
    response.setContentType("data/html;");
    response.getWriter().println(comment);
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
