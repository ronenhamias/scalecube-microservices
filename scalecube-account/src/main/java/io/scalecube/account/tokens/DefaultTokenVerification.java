package io.scalecube.account.tokens;

import io.scalecube.account.api.Organization;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;
import io.scalecube.account.db.RedisOrganizations;
import io.scalecube.jwt.WebToken;

import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultTokenVerification implements TokenVerifier {

  private ConcurrentMap<String, WebToken> jwtProviders = new ConcurrentHashMap<>();

  private final RedisOrganizations organizations;

  public DefaultTokenVerification(RedisOrganizations organizations) {
    this.organizations = organizations;
  }

  @Override
  public User verify(Token token) throws Exception {
    String key = "account-service/" + token.origin();
    Organization org = organizations.getOrganization(token.origin());

    if (org != null) {
      WebToken jwt = jwtProviders.computeIfAbsent(key, j -> new WebToken("account-service", org.id()));
      Claims claims = jwt.parse(token.token(), org.secretKey());
      Map<String, String> claimsMap = new HashMap<String, String>();
      for (Entry<String, Object> entry : claims.entrySet()) {
        claimsMap.put(entry.getKey(), entry.getValue().toString());
      }
      if (claims != null) {
        return new User(org.id(), org.email(), true, org.name(), null, null, null, null, claimsMap);
      }
    }
    return null;
  }
}
