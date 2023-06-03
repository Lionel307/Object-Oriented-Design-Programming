package unsw.sso.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import unsw.sso.Token;
import unsw.sso.Browser;

public class Hoogle implements Provider{
    private Map<String, String> userMappings = new HashMap<>();
    @Override
    public void addUser(String email, String password) {
        userMappings.put(email, password);
    }
    
    @Override
    public Token getToken(String accessToken, String email, String provider) {
        if (Objects.equals(userMappings.get(email), accessToken)) {
            return new Token(UUID.randomUUID().toString(), email, provider);
        } else {
            return new Token(null, email, provider);
        }
    }
}
//     public Token generateFormSubmission(String email, String password) {
//         if (Objects.equals(userMappings.get(email), password)) {
//             return new Token(UUID.randomUUID().toString(), email, getClass().getSimpleName());
//         } else {
//             return new Token(null, email, getClass().getSimpleName());
//         }
//     }
// }
