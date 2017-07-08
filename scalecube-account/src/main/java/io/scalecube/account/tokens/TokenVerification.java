package io.scalecube.account.tokens;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;
import io.scalecube.account.db.RedisOrganizations;

public class TokenVerification {

  private static final String GOOGLE = "google";
  private static final String DEFAULT_TOKEN_PROVIDER = "default";
  private final ConcurrentMap<String, TokenVerifier> providers = new ConcurrentHashMap<>();
  
  public TokenVerification(RedisOrganizations organizations){
    
    providers.putIfAbsent(GOOGLE, new GoogleTokenVerification());
    providers.putIfAbsent(DEFAULT_TOKEN_PROVIDER, new DefaultTokenVerification(organizations));
  
  }
  
  public User verify(Token token) throws Exception {
    if(providers.containsKey(token.origin()))
      return providers.get(token.origin()).verify(token);
    else{
      return providers.get(DEFAULT_TOKEN_PROVIDER).verify(token);
    } 
  }
}
