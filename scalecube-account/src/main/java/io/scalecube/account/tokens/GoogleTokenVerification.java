package io.scalecube.account.tokens;

import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

public class GoogleTokenVerification implements TokenVerifier {

  private static final JacksonFactory jacksonFactory = new JacksonFactory();
  private static final HttpTransport transport = new NetHttpTransport();
  private static final String CLIENT_ID = "132044490342-vj48declsaj0k2f72hn0t79dsoid5kvc.apps.googleusercontent.com";

  private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
      .setAudience(Collections.singletonList(CLIENT_ID))
      .build();

  @Override
  public User verify(Token token) throws Exception {
    // google profile
    final GoogleIdToken idToken = verifier.verify(token.token());
    if (idToken != null) {
      return getUserProfile(idToken);
    } else {
      return null;
    }
  }

  private User getUserProfile(GoogleIdToken idToken) {
    Payload payload = idToken.getPayload();

    // Get profile information from payload
    User user = new User(payload.getSubject(),
        payload.getEmail(),
        Boolean.valueOf(payload.getEmailVerified()),
        (String) payload.get("name"),
        (String) payload.get("picture"),
        (String) payload.get("locale"),
        (String) payload.get("family_name"),
        (String) payload.get("given_name"),
        null);

    return user;
  }

}
