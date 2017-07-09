package io.scalecube.account.db;

import io.scalecube.account.api.Organization;
import io.scalecube.account.api.OrganizationMember;
import io.scalecube.account.api.User;

import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedisOrganizations {

  private static final String ORGANIZATIONS_BY_ID = "organizations_by_id";
  private static final String ORGANIZATION_MEMBERS = "organization_members";
  private static final String USERS = "users";

  private final RedissonClient redisson;

  public RedisOrganizations(RedissonClient redisson) {
    this.redisson = redisson;
  }

  public void clearAll() {
    redisson.getKeys().deleteByPattern("*" + ORGANIZATION_MEMBERS);
    redisson.getMap(ORGANIZATIONS_BY_ID).clear();
    redisson.getMap(USERS).clear();
  }

  public void register(User user) {
    ConcurrentMap<String, User> users = redisson.getMap(USERS);
    users.putIfAbsent(user.id(), user);
  }

  public User getUser(String userId) {
    ConcurrentMap<String, User> users = redisson.getMap(USERS);
    return users.get(userId);
  }

  /**
   * create new organization owned by the creator user.
   * 
   * @param owner of the organization.
   * @param organization newly created organization.
   * @return organization newly created organization
   * @throws AccessPermissionException if user has not permissions.
   */
  public Organization createOrganization(User owner, Organization organization) throws AccessPermissionException {
    ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
    organizations.putIfAbsent(organization.id(), organization);

    final ConcurrentMap<String, OrganizationMember> members =
        redisson.getMap(organizationMembersCollection(organization.id()));
    members.putIfAbsent(owner.id(), new OrganizationMember(owner, "owner"));

    return organization;
  }

  /**
   * get Organization by a given organization id.
   * 
   * @param id of the requested organization.
   * @return Organization instance.
   */
  public Organization getOrganization(String id) {
    ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
    return (Organization) organizations.get(id);
  }

  /**
   * delete Organization from the store.
   * 
   * @param owner of the requesting user to delete.
   * @param org to delete.
   */
  public void deleteOrganization(User owner, Organization org) {
    if (owner.id().equals(org.ownerId()) 
        && getOrganizationMembers(org.id()).size() == 1 
        && getOrganizationMembers(org.id()).contains(owner)) {

      ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
      organizations.remove(org.id());
    }
  }

  /**
   * returns the Organization a specific user is member in.
   * 
   * @param user in subject.
   * @return the organizations this user is memeber in.
   */
  public Collection<Organization> getUserMembership(final User user) {
    ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
    return Collections.unmodifiableCollection(organizations.values().stream()
        .filter(org -> this.isOrganizationMember(user, org))
        .collect(Collectors.toList()));
  }

  /**
   * Query organizations with specific predicate.
   * 
   * @param predicate to query by.
   * @return organizations match the predicate.
   */
  public List<Organization> queryBy(Predicate<? super Organization> predicate) {
    ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
    if (!organizations.isEmpty()) {
      List<Organization> orgs = organizations.values().stream()
          .filter(predicate)
          .collect(Collectors.toList());
      return orgs;
    } else {
      return Collections.unmodifiableList((new ArrayList<Organization>()));
    }

  }

  /**
   * update organizations details.
   * 
   * @param owner requesting to update.
   * @param org to be updated.
   * @param newDetails to update with.
   */
  public void updateOrganizationDetails(User owner, Organization org, Organization newDetails) {
    if (org.id().equals(newDetails.id())) {
      ConcurrentMap<String, Organization> organizations = redisson.getMap(ORGANIZATIONS_BY_ID);
      organizations.put(org.id(), newDetails);
    }
  }

  /**
   * Returns list of organizations members by a given organization id.
   * 
   * @param organizationId in question.
   * @return collection of the organization members of the given organization id.
   */
  public Collection<OrganizationMember> getOrganizationMembers(String organizationId) {
    final ConcurrentMap<String, OrganizationMember> members =
        redisson.getMap(organizationMembersCollection(organizationId));
    return Collections.unmodifiableCollection(members.values());
  }


  /**
   * invite a user to the organization.
   * 
   * @param owner requesting to invite.
   * @param organization to invite the user to.
   * @param user to be invited
   * @throws AccessPermissionException in case owner has not rights to invite.
   */
  public void invite(User owner, Organization organization, final User user) throws AccessPermissionException {
    if (isOwner(organization, owner)) {
      final ConcurrentMap<String, OrganizationMember> members =
          redisson.getMap(organizationMembersCollection(organization.id()));

      if (owner.id().equals(user.id())) {
        members.putIfAbsent(user.id(), new OrganizationMember(user, "owner"));
      } else {
        members.putIfAbsent(user.id(), new OrganizationMember(user, "member"));
      }
    } else {
      throw new AccessPermissionException(
          "user: " + owner.name() + " id: " + owner.id() + " is not an owner of organization: " + organization.id());
    }
  }

  /**
   * leave organization.
   * 
   * @param organization to leave.
   * @param user requesting to leave.
   */
  public void leave(Organization organization, User user) {
    final ConcurrentMap<String, OrganizationMember> members =
        redisson.getMap(organizationMembersCollection(organization.id()));
    members.remove(user.id());
  }

  /**
   * Kickout user from the organization.
   * 
   * @param owner of the organization that will do the kickout.
   * @param org to kickout the user from.
   * @param member to kickout.
   */
  public void kickout(User owner, Organization org, User member) {
    if (isOwner(org, owner) && isOrganizationMember(member, org)) {
      leave(org, member);
    }
  }

  /**
   * return the user list that are owners for the organization.
   * 
   * @param organization in question.
   * @return list of onwers.
   */
  public List<User> getOwners(Organization organization) {
    final ConcurrentMap<String, OrganizationMember> members =
        redisson.getMap(organizationMembersCollection(organization.id()));

    return Collections.unmodifiableList(members.values().stream()
        .filter(m -> m.role().equals("owner"))
        .map(om -> om.user())
        .collect(Collectors.toList()));
  }

  /**
   * search user by username or email.
   * 
   * @param fullNameOrEmail username or email to search by.
   * @return list of users matching the search criteria.
   */
  public List<User> searchUserByUserNameOrEmail(String fullNameOrEmail) {
    ConcurrentMap<String, User> users = redisson.getMap(USERS);
    return Collections.unmodifiableList(
        users.values().stream().filter(q -> {
          return startsWith(() -> q.name(), fullNameOrEmail) || startsWith(() -> q.email(), fullNameOrEmail);
        }).collect(Collectors.toList()));

  }

  private String organizationMembersCollection(String organizationId) {
    return organizationId + "@" + ORGANIZATION_MEMBERS;
  }

  private boolean isOwner(Organization organization, User user) {
    return getOwners(organization).stream().filter(u -> u.id().equals(user.id())).count() > 0;
  }

  private boolean isOrganizationMember(User user, Organization organization) {
    final ConcurrentMap<String, OrganizationMember> members =
        redisson.getMap(organizationMembersCollection(organization.id()));
    return members.containsKey(user.id());
  }

  private boolean startsWith(Supplier<String> func, String fullName) {
    return func.get().trim().toLowerCase()
        .startsWith(fullName.trim().toLowerCase());
  }
}
