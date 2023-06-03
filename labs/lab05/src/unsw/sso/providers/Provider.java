package unsw.sso.providers;

import unsw.sso.Token;

public interface Provider {
    public Token getToken(String accessToken, String email, String provider);
    public void addUser(String email, String password);
    
}
