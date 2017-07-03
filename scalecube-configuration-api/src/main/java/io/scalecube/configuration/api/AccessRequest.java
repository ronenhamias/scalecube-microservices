package io.scalecube.configuration.api;

import io.scalecube.account.api.Token;

public interface AccessRequest {

  String collection();

  Token token();

}
