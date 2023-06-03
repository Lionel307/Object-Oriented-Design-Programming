package unsw.sso;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import unsw.sso.providers.Hoogle;

public class Browser {
    private Token currentToken = null;
    private String currentPage = null;
    private String previousPage = null;
    private ClientApp currentApp = null;
    private HashMap<String, Token> allTokens = new HashMap<String, Token>();

    public void visit(ClientApp app) {
        allTokens.put(currentPage, currentToken);
        currentToken = null;
        this.previousPage = null;
        this.currentPage = "Select a Provider";
        this.currentApp = app;
    }

    public String getCurrentPageName() {
        return this.currentPage;
    }

    public void clearCache() {
        allTokens = new HashMap<String, Token>();
    }

    public void interact(Object using) {
        if (using == null) {
            this.currentPage = this.previousPage;
            if (this.currentPage == null) {
                this.visit(this.currentApp);
            }
            
            return;
        }

        switch (currentPage) {
            case "Select a Provider": {
                // if the currentApp doesn't have hoogle
                // then it has no providers, which just will prevent
                // transition.
                if (using instanceof Hoogle && currentApp.hasHoogle()) {
                    this.previousPage = currentPage;
                    this.currentPage = "Hoogle Login";
                } else {
                    // do nothing...
                }
                break;
            }
            case "Hoogle Login": {
                if (using instanceof Token) {
                    this.previousPage = currentPage;
                    this.currentPage = "Home";

                    // tell client application about us
                    this.currentToken = (Token)using;
                    this.currentApp.registerUser((Token)using);
                } else {
                    // do nothing...
                }
                break;
            }
            case "Home": {
                // no need to do anything
                break;
            }
        }
    }
}
