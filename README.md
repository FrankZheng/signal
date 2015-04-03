# signal
A lightweight signal / handler framework for java

A signal / handler could be used to implement the classic observer pattern. It's better than the traditional "listener" way.
Following is a sample that demonstrate how to use signal / handler to implement a login / logout logic.

```java
public class LoginModel {
  private Signal<String> loggedIn = new Signal<String>();
  private Signal<Void> loggedOut = new Signal<Void>();
  
  public void login(String username, String password) {
    //make some network calls, get a user token from server for example.
    //dispatch the sigal
    loggedIn.dispatch(userToken);
  }
  
  public void logout() {
    //dispatch the signal
    loggedOut.dispatch();
  }
}

//Use the LoginModel 
LoginModel.getInstance().getLoggedIn().add( new Handler<String>() {
  @Override
  public void onDispatch(String token) {
    //user logged in, save the token, and do other stuff
  }
});

LoginModel.getInstance().getLoggedOut().add( new Handler<Void>() {
  @Override
  public void onDispatch(Void) {
    //user logged out, say goodbye
  }
});


```

