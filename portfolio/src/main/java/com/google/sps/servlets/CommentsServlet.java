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
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {

//   private List<String> comments;

//   @Override
//   public void init() {
//     // comments = new ArrayList<>();
//     // comments.add("Loved the styling of this webpage");
//     // comments.add("Your dog is adorable");
//     // comments.add("Do you like pizzas or burgers");
//     // comments.add("The weather in Hyderabad is quite sunny");
//     // comments.add("Google is awesome!");
//   }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Query query = new Query("comment").addSort("time", SortDirection.DESCENDING);
    // Query query = new Query("comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<String> comments = new ArrayList<>();

    for(Entity entity : results.asIterable()){
        String comment = (String) entity.getProperty("comment");
        comments.add(comment);
    }

    String json = convertToJsonUsingGson(comments);
    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String comment = request.getParameter("new-comment");
    //   comments.add(comment);
      long timestamp = System.currentTimeMillis();

      Entity newcomment = new Entity("comment");
      newcomment.setProperty("comment",comment);
      newcomment.setProperty("time",timestamp);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(newcomment);
      response.sendRedirect("/index.html");
  }
  private String convertToJsonUsingGson(List<String> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}
