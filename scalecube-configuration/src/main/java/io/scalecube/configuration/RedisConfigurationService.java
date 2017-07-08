package io.scalecube.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.redisson.api.RedissonClient;

import io.scalecube.account.api.AccountService;
import io.scalecube.configuration.api.AccessRequest;
import io.scalecube.configuration.api.Acknowledgment;
import io.scalecube.configuration.api.ConfigurationService;
import io.scalecube.configuration.api.DeleteRequest;
import io.scalecube.configuration.api.Entries;
import io.scalecube.configuration.api.FetchRequest;
import io.scalecube.configuration.api.FetchResponse;
import io.scalecube.configuration.api.InvalidPermissionsException;
import io.scalecube.configuration.api.SaveRequest;
import io.scalecube.configuration.db.RedisStore;
import io.scalecube.services.annotations.ServiceProxy;

public class RedisConfigurationService implements ConfigurationService {

  @ServiceProxy
  private AccountService accountService;

  private final RedisStore<Document> store;
  
  public RedisConfigurationService(RedissonClient client){
    store = new RedisStore<Document>(client);
  }
  private static final String PERMISSIONS_LEVEL = "permissions-level";

  private enum Permissions {
    read, write
  }

  public CompletableFuture<FetchResponse> fetch(final FetchRequest request) {
    final CompletableFuture<FetchResponse> future = new CompletableFuture<>();
    accountService.register(request.token()).whenComplete((success, error) -> {
      if (error == null) {
        final Document result = store.get(getCollectionName(request), request.key());
        if(result!=null) {
          future.complete(FetchResponse.builder()
              .key(result.key())
              .value(result.value())
              .build());
        } else {
          future.complete(FetchResponse.builder().build());
        }
      } else {
        future.completeExceptionally(error);
      }
    });
    return future;
  }

  private String getCollectionName(final AccessRequest request) {
    return request.collection() + "@" + request.token().origin();
  }

  @Override
  public CompletableFuture<Entries<FetchResponse>> entries(final FetchRequest request) {
    final CompletableFuture<Entries<FetchResponse>> future = new CompletableFuture<>();
    accountService.register(request.token()).whenComplete((success, error) -> {
      if (error == null) {
        final Collection<Document> result = store.entries(getCollectionName(request));
        List<FetchResponse> resultSet =
            result.stream().map(doc -> FetchResponse.builder().key(doc.key()).value(doc.value()).build())
                .collect(Collectors.toList());

        future.complete(new Entries((FetchResponse[]) resultSet.toArray(new FetchResponse[resultSet.size()])));
      } else {
        future.completeExceptionally(error);
      }
    });
    return future;
  }

  @Override
  public CompletableFuture<Acknowledgment> save(SaveRequest request) {

    final CompletableFuture<Acknowledgment> future = new CompletableFuture<>();
    accountService.register(request.token()).whenComplete((success, error) -> {
      if (error == null) {
        if (getPermissions(success.claims()).ordinal() >= Permissions.write.ordinal()) {
          Document doc = Document.builder()
              .key(request.key())
              .value(request.value())
              .build();

          store.put(getCollectionName(request), doc.key(), doc);
          future.complete(new Acknowledgment(true));
        } else {
          future.completeExceptionally(
              new InvalidPermissionsException("invalid permissions-level save request requires write access"));
        }
      } else {
        future.completeExceptionally(error);
      }
    });

    return future;
  }


  @Override
  public CompletableFuture<Acknowledgment> delete(DeleteRequest request) {

    final CompletableFuture<Acknowledgment> future = new CompletableFuture<>();
    accountService.register(request.token()).whenComplete((success, error) -> {
      if (error == null) {
        if (getPermissions(success.claims()).ordinal() >= Permissions.write.ordinal()) {
          store.remove(getCollectionName(request), request.key());
          future.complete(new Acknowledgment(true));
        } else {
          future.completeExceptionally(
              new InvalidPermissionsException("invalid permissions-level delete request requires write access"));
        }
      } else {
        future.completeExceptionally(error);
      }
    });

    return future;
  }

  private Permissions getPermissions(Map<String, String> claims) {
    if (claims != null && claims.containsKey(PERMISSIONS_LEVEL)) {
      return Permissions.valueOf(claims.get(PERMISSIONS_LEVEL));
    }
    return Permissions.read;
  }
}
