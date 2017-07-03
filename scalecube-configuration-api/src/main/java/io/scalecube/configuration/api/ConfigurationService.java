package io.scalecube.configuration.api;

import java.util.concurrent.CompletableFuture;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

@Service
public interface ConfigurationService {

  @ServiceMethod
  public CompletableFuture<FetchResponse> fetch(FetchRequest request);
  
  
  @ServiceMethod
  public CompletableFuture<Acknowledgment> save(SaveRequest request);

  @ServiceMethod
  public CompletableFuture<Entries<FetchResponse>> entries(FetchRequest request);

  @ServiceMethod
  public CompletableFuture<Acknowledgment> delete(DeleteRequest request);
  
}
