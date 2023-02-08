# Exercise: Implement User Authentication Using OpenID Connect (OIDC)

## Goals

Implement authentication using OpenID Connect (OIDC) within a new Spring Boot application.

## Details

By leveraging an identity provider, it's possible to get out of the business of storing user passwords and/or identity information. In this exercise, you'll use Spring Security's support OpenID Connect (OIDC) to allow users to authentication with external identity providers such as GitHub and Google.

## Steps

### 1. Create a New Project

Create a new Maven project with the following project values:

* `groupId`: learn
* `artifactId`: openid-connect-example

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>learn</groupId>
    <artifactId>openid-connect-example</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.8</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

</project>
```

#### Add an App Class

* Add package named `learn.openidconnect`
* Add a class named `App` to the `learn.openidconnect` package
* Add the following content to the `App` class:

**App.java**

```java
package learn.openidconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

#### Add a Home Page

* Add a directory named `static` to the `resources` directory
* Add an HTML page named `index.html` to the `static` directory
* Add the following content to the `index.html` file:

**resources/static/index.html**

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <title>OIDC Demo</title>
</head>
<body>
  <h1>OIDC Demo</h1>
  <div class="container"></div>
</body>
</html>
```

> **Note:** The `index.html` page could be replaced by a production build of a React application. For this exercise, we'll keep things as simple as possible just write vanilla HTML and JavaScript.

#### Testing

_Open a private browsing tab and browse to `http://localhost:8080`. You should see your `index.html` page rendered in the browser._

### 2. Securing the Application with GitHub and Spring Security

To make the application secure, you can simply add Spring Security as a dependency. Since we're wanting to do a "social" login (i.e. delegate to GitHub), we can be more specific by adding the Spring Security OAuth 2.0 Client starter:

**pom.xml**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

Adding that dependency will secure your app with OAuth 2.0 by default.

#### Add a New GitHub App

Browse to [https://github.com/settings/developers](https://github.com/settings/developers) to configure a new app in GitHub.

Select "New OAuth App" and to view the "Register a new OAuth application" page. Enter an application name (e.g. "Prime OIDC Example") and optionally a description. Then, enter the "Homepage URL" as `http://localhost:8080`. Finally, set the "Authorization callback URL" to `http://localhost:8080/login/oauth2/code/github` and click "Register Application".

On the next page, click the "Generate a new client secret" button and copy the "Client ID" and "Client secret" values to a secure location on your local machine.

> **Note:** If you're using a Git repo for your project, don't commit your "Client ID" and "Client secret" values. You want to keep those values from propagating to GitHub.

#### Configure `application.properties`

* Add a file named `application.properties` to the `resources` directory
* Add the following content to the `application.properties` file:

**resources/application.properties**

```
spring.security.oauth2.client.registration.github.clientId=${GH_CLIENT_ID}
spring.security.oauth2.client.registration.github.clientSecret=${GH_CLIENT_SECRET}
```

#### Configure Environment Variables

Use the IntelliJ "Run > Edit Configurations..." menu option to configure the `GH_CLIENT_ID` and `GH_CLIENT_SECRET` environment variables for the "Application" run configuration template. Set the `GH_CLIENT_ID` and `GH_CLIENT_SECRET` environment variable values respectively to your "Client ID" and "Client secret" values.

#### Testing

_Run the application. Open a private browsing tab and browse to `http://localhost:8080`. You should be redirected to GitHub's website to authorize your application. Click the "Authorize" button. You should see your `index.html` page rendered in the browser._

#### Login Page

Spring Security provides an automatically generated Login page (see `http://localhost:8080/login`) that'll display all configured identity providers:

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link href="https://getbootstrap.com/docs/4.0/examples/signin/signin.css" rel="stylesheet" crossorigin="anonymous"/>
  </head>
  <body>
    <div class="container">
      <h2 class="form-signin-heading">Login with OAuth 2.0</h2>
      <table class="table table-striped">
        <tr>
          <td>
            <a href="/oauth2/authorization/github">GitHub</a>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>
```

#### What Just Happened?

Your application, using OAuth 2.0 terminology, is a "Client Application". It uses the authorization code grant to obtain an access token from GitHub (the "Authorization Server"). The client application uses the access token to ask GitHub, the "Resource Server", for some of your personal details, but only what you permitted GitHub to do. Assuming that process is successful, the client application adds your user details to the Spring Security context so that you are authenticated.

#### Debugging

Using the "Network" tab in the browser's dev tools, you can observe the flow of HTTP requests between the application and GitHub. At the end of the process, you can see the cookie (named `JSESSIONID` by default) that Spring Security generates to persist your authentication state across requests.

The requests that the application makes to get an access token and your personal information, occurs between the application server and the authorization server, and the application server and the resource server. Because the browser isn't used to send those requests, you can't observe them using the browser's dev tools. To observe those requests within IntelliJ, you need to set a new configuration option within the `application.properties` file:

```
logging.level.org.springframework.web.client.RestTemplate=DEBUG
```

> **Note:** Be sure to restart the server if it's currently running to pickup the changes to the `application.properties` file.

Here's the HTTP request to the authorization server (from the output window in IntelliJ):

```
2022-06-27 21:34:59.307 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : HTTP POST https://github.com/login/oauth/access_token
2022-06-27 21:34:59.308 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Accept=[application/json, application/*+json]
2022-06-27 21:34:59.308 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Writing [{grant_type=[authorization_code], code=[054ee58ce4050bf181f8], redirect_uri=[http://localhost:8080/login/oauth2/code/github]}] as "application/x-www-form-urlencoded;charset=UTF-8"
2022-06-27 21:34:59.757 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Response 200 OK
2022-06-27 21:34:59.761 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Reading to [org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse] as "application/json;charset=utf-8"
```

Then, after the server receives the access token from the authorization server, an HTTP request is sent to the resource server to get your user information:

```
2022-06-27 21:34:59.825 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : HTTP GET https://api.github.com/user
2022-06-27 21:34:59.825 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Accept=[application/json, application/*+json]
2022-06-27 21:35:00.153 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Response 200 OK
2022-06-27 21:35:00.153 DEBUG 53656 --- [nio-8080-exec-3] o.s.web.client.RestTemplate              : Reading to [java.util.Map<java.lang.String, java.lang.Object>]
```

### 3. Add a Welcome Page

Instead of immediately redirecting the user to GitHub, we can update the `index.html` page to:

* Display a link to authenticate using GitHub
* Display information about the user after they've authenticated

#### Getting the User

Start with adding a controller to define:

* An endpoint to retrieve all of the authenticated user's information
* An endpoint to retrieve the authenticated user's name

**controllers/UserController.java**

```java
package learn.openidconnect.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User principal) {
        // It's not a great idea to return a whole OAuth2User in an endpoint
        // since it might contain information you would rather not reveal to a browser client.
        return principal;
    }

    @GetMapping("/name")
    public Map<String, Object> getUserName(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    // Alternative approach to getting the principal user that uses the security context...

//    @GetMapping("/context")
//    public ResponseEntity<?> getPrincipalFromSecurityContext() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User principal = (OAuth2User)authentication.getPrincipal();
//            return new ResponseEntity<>(principal, HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
```

> For the list of standard OIDC claims see [this page in the official specification](https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims).

#### Testing

_Run the application. After authenticating using GitHub, open another browser tab (within the same window), and browse to `http://localhost:8080/api/user` to see your user information. Browse to `http://localhost:8080/api/user/name` to see your name._

#### Conditional Content on the Home Page

With the API endpoints in place, you can update the `index.html` page to dynamically render content:

**resources/static/index.html**

```html
<h1>OIDC Demo</h1>
<div id="unauthenticated">
  With GitHub: <a href="/oauth2/authorization/github">click here</a>
</div>
<div id="authenticated" style="display:none">
  Logged in as: <span id="user"></span>
</div>
<script src="/js/main.js"></script>
```

**resources/static/js/main.js**

```js
const unauthenticatedDiv = document.getElementById('unauthenticated');
const authenticatedDiv = document.getElementById('authenticated');
const userNameSpan = document.getElementById('user');

async function getUserName() {
  const response = await fetch('/api/user/name');
  const data = await response.json();

  if (data.name) {
    authenticatedDiv.style.display = 'block';
    unauthenticatedDiv.style.display = 'none';
    userNameSpan.innerHTML = data.name;
  }
}

getUserName();
```

#### Making the Home Page Public

This application will still authenticate as before, but it's still going to redirect before showing the page. To display the `index.html` page before the user authenticates (so they can decide if they want to click the link to authenticate), you need to allow anonymous access to the `index.html` page by defining a `SecurityFilterChain` bean:

**security/SecurityConfig.java**

```java
package learn.openidconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(a -> a
                        .antMatchers("/", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login()
                        .defaultSuccessUrl("/", true);

        return http.build();
    }
}
```

The above configuration allows anonymous access to two routes, `/` (home) and `/error`, while requiring authentication for every other route.

> The `/error` route is a special Spring Boot endpoint for displaying errors.

The `authenticationEntryPoint()` method is used to configure Spring Security to return a `401 UNAUTHORIZED` response status code instead of redirecting to a login page if a request fails because of the lack of authentication.

By default, Spring Security will direct to the last failed request which was our request to the `main.js` file. We need to redirect to `index.html`, so we call the `defaultSuccessUrl()` method to configure the default redirection URL.

#### Testing

_Run the application. This time, instead of being automatically redirect to GitHub, you'll see the `index.html` page displayed in the browser. Click on the link to authenticate with GitHub. After the authentication process completes, you'll see your name displayed on the `index.html` page._

### 4. Login with Google

Let's add Google as a second authentication option.

#### Add a New Google App

To use Google's OAuth 2.0 authentication system for login, you must set up a project in the [Google API Console](https://console.cloud.google.com/apis/credentials) to obtain OAuth 2.0 credentials.

Click on "Create Credentials" and select "OAuth client ID" for the credentials type. For the "Application type" select "Web application". Provide a name (i.e. "Prime OIDC Example"). Under "Authorized JavaScript origins" add the URI `http://localhost:8080`. Under "Authorized redirect URIs" add the URI `http://localhost:8080/login/oauth2/code/google`. Click the "Create" button to complete the process.

In the popup window, copy the "Client ID" and "Client Secret" values to a secure location on your local machine.

#### Update the `application.properties` File

Add the following lines to the `application.properties` file:

**resources/application.properties**

```
spring.security.oauth2.client.registration.google.clientId=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.clientSecret=${GOOGLE_CLIENT_SECRET}
```

#### Configure Environment Variables

Use the IntelliJ "Run > Edit Configurations..." menu option to configure the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variables for the "Application" run configuration template. Set the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variable values respectively to your "Client ID" and "Client Secret" values.

#### Update the `index.html` Page

And finally, update the `index.html` page:

**resources/static/index.html**

```html
<div id="unauthenticated">
  <div>
    With GitHub: <a href="/oauth2/authorization/github">click here</a>
  </div>
  <div>
    With Google: <a href="/oauth2/authorization/google">click here</a>
  </div>
</div>
```

#### Testing

_Run the application. Click the link to authenticate using Google. After the authentication process completes, you'll see your name displayed on the `index.html` page._

### 5. Logout

You can also give your users a way to explicitly logout of your application.

#### Logout Endpoint

Start with adding a new endpoint to the `UserController` class:

**controllers/UserController.java**

```java
@DeleteMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request) {
    try {
        request.logout();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (ServletException e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
```

The [`HttpServletRequest`](https://tomcat.apache.org/tomcat-7.0-doc/servletapi/javax/servlet/http/HttpServletRequest.html) type defines a method named `logout()` that we can call to remove any authenticated user from the request. Calling this method removes the user's session on the server.

#### Disable CSRF Protection for All API Endpoints

By default, Spring Security requires a CSRF (cross-site request forgery) token for all `POST` requests. This interferes with being able to use `fetch` from the browser to make `POST` requests. To resolve this issue, you can update the security configuration to disable CSRF protection on all API endpoints:

**security/SecurityConfig.java**

```java
package learn.openidconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // NEW!!!
                .csrf()
                        .ignoringAntMatchers("/api/**").and()
                .authorizeRequests(a -> a
                        .antMatchers("/", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login()
                        .defaultSuccessUrl("/", true);

        return http.build();
    }
}
```

#### Add Logout Button

Update the `index.html` page:

**resources/static/index.html**

```html
<div id="authenticated" style="display:none">
    <p>Logged in as: <span id="user"></span></p>
    <p><button id="logout">Logout</button></p>
</div>
```

And finally, update the `main.js` file to get a reference to the `<button>` element and define a click handler for it:

**resources/static/js/main.js**

```js
const unauthenticatedDiv = document.getElementById('unauthenticated');
const authenticatedDiv = document.getElementById('authenticated');
const userNameSpan = document.getElementById('user');
// new!
const logoutButton = document.getElementById('logout');

async function getUserName() {
  const response = await fetch('/api/user/name');
  const data = await response.json();

  if (data.name) {
    authenticatedDiv.style.display = 'block';
    unauthenticatedDiv.style.display = 'none';
    userNameSpan.innerHTML = data.name;

    // new!
    logoutButton.addEventListener('click', async () => {
        const response = await fetch('/api/user/logout', { method: 'DELETE' });
        if (response.status === 204) {
            window.location = '/';
        } else {
            console.log(`Unexpected status code: ${response.status}`);
        }
    });
  }
}

getUserName();
```

#### Testing

_Run the application. Click one of the links to authenticate. You should see your name displayed on the `index.html` page. Click the "Logout" button. You should now be logged out._
