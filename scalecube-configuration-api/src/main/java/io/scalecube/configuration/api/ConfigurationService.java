package io.scalecube.configuration.api;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

import reactor.core.publisher.Mono;

/**
 * Configuration services manages Key / value json objects store. clients of the configuration service may save fetch
 * list and delete entries from their store. read operations such as fetch and listing requires read level permissions.
 * write operations such as save and delete requires write level operations. to verify the permissions each request to
 * the configuration service consists with a Token, this token is created and managed by the account service. the
 * configuration service validates the token with the account service.
 */
@Service
public interface ConfigurationService {

  /**
   * Fetch request requires read level permissions to get entry object from the store.
   * 
   * @param request includes the collection and key of the requested object.
   * @return json object from the store.
   */
  @ServiceMethod
  Mono<FetchResponse> fetch(FetchRequest request);

  /**
   * Entries request requires read level permissions to list all entries objects from the store.
   * 
   * @param request includes the name of the collection to list.
   * @return list of FetchReponses per each entry in the collection.
   */
  @ServiceMethod
  Mono<Entries<FetchResponse>> entries(FetchRequest request);


  /**
   * Save request requires write level permissions to save (create or update) entry to the store.
   * 
   * @param request includes the name of the collection, key, value to save.
   * @return acknowledgement when saved.
   */
  @ServiceMethod
  Mono<Acknowledgment> save(SaveRequest request);

  /**
   * delete request requires write level permissions to delete entry from the store.
   * 
   * @param request includes the name of the collection, key to delete.
   * @return acknowledgement when deleted.
   */
  @ServiceMethod
  Mono<Acknowledgment> delete(DeleteRequest request);

}
