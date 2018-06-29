package io.scalecube.configuration;

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
import io.scalecube.configuration.db.redis.RedisStore;
import io.scalecube.services.annotations.Inject;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RedisConfigurationService implements ConfigurationService {

  private static final String PERMISSIONS_LEVEL = "permissions-level";
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfigurationService.class);
  
  @Inject
  private AccountService accountService;

  private final RedisStore<Document> store;

  public static class Builder {

    private RedissonClient redisson;
    private AccountService accountService;

    public Builder redisson(RedissonClient redisson) {
      this.redisson = redisson;
      return this;
    }

    public Builder mock(AccountService accountService) {
      this.accountService = accountService;
      return this;
    }

    /**
     * build a Service instance.
     * 
     * @return new initialzed service instance.
     */
    public RedisConfigurationService build() {
      return new RedisConfigurationService(new RedisStore<Document>(redisson), accountService);
    }

  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  private RedisConfigurationService(RedisStore<Document> store, AccountService accountService) {
    this.store = store;
    this.accountService = accountService;
  }

  private enum Permissions {
    read, write
  }

  @Override
  public CompletableFuture<FetchResponse> fetch(final FetchRequest request) {
    
    LOGGER.debug("recived fetch request {}",request);
    final CompletableFuture<FetchResponse> future = new CompletableFuture<>();
    accountService.register(request.token()).whenComplete((success, error) -> {
      if (error == null) {
        final Document result = store.get(getCollectionName(request), request.key());
        if (result != null) {
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
          future.complete(new Acknowledgment());
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
          future.complete(new Acknowledgment());
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
