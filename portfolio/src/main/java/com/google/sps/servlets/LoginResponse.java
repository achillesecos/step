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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.sps.data.User;

@WebServlet("/login-response")
public class LoginResponse extends HttpServlet {

  public boolean isLoggedIn;
  public String userEmail;
  public User user;
  public Gson gson;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("text/html");
    gson = new Gson();
    
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      isLoggedIn = true;
      userEmail = userService.getCurrentUser().getEmail();
      
      String urlToRedirectToAfterUserLogsOut = "/index.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      String logoutResponse = "<p>Goodbye " + userEmail + "!</p>";
      response.getWriter().println(logoutResponse);
      String logoutTag = "<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>";
      response.getWriter().println(logoutTag);
    } else {
      isLoggedIn = false;
      userEmail = null;

      String urlToRedirectToAfterUserLogsIn = "/index.html";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      String loginResponse = "<p>Hello! Thanks for visiting my personal website.</p>";
      response.getWriter().println(loginResponse);
      String loginTag = "<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>";
      response.getWriter().println(loginTag);
    }
  }
}