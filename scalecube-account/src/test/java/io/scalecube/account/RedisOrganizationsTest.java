package io.scalecube.account;

import io.scalecube.account.api.Organization;
import io.scalecube.account.api.User;
import io.scalecube.account.db.AccessPermissionException;
import io.scalecube.account.db.RedisOrganizations;
import io.scalecube.testlib.BaseTest;

import org.junit.Test;
import org.redisson.Redisson;

import java.util.Collection;
import java.util.List;

public class RedisOrganizationsTest extends BaseTest {

  private static final String ORG_NAME = "testOrg4";

  @Test
  public void testRedisOrganizationMembers() throws AccessPermissionException {
    RedisOrganizations organizations = new RedisOrganizations(Redisson.create());
    User owner = new User("id0", "email0", true, "name0", "pictureUrl0", "locale0", "familyName0", "givenName0", null);
    Organization org;
    List<Organization> resultSet = organizations.queryBy(v -> v.name().equals(ORG_NAME));


    if (resultSet.isEmpty())
      org = organizations.createOrganization(owner, Organization.builder()
          .name(ORG_NAME)
          .ownerId(owner.id())
          .secretKey("YYYYYYY")
          .email("e@mail.com")
          .build());

    else {
      org = resultSet.get(0);
    }
    organizations.updateOrganizationDetails(owner, org, Organization.builder().copy(org));
    Collection<Organization> userOrgs = organizations.getUserMembership(owner);
    System.out.println(userOrgs);


    User member1 =
        new User("id1", "email0", true, "name0", "pictureUrl0", "locale0", "familyName0", "givenName0", null);
    organizations.invite(owner, org, member1);
    organizations.invite(owner, org,
        new User("id2", "email2", true, "name2", "pictureUrl2", "locale2", "familyName2", "givenName2", null));
    System.out.println(organizations.getOrganizationMembers(org.id()));

    organizations.leave(org,
        new User("id2", "email2", true, "name2", "pictureUrl2", "locale2", "familyName2", "givenName2", null));
    System.out.println(organizations.getOrganizationMembers(org.id()));

    organizations.kickout(owner, org, member1);

    System.out.println(organizations.getOrganizationMembers(org.id()));

    System.out.println(organizations.getOwners(org));

    organizations.deleteOrganization(owner, org);

    System.out.println(organizations.getUserMembership(owner));
  }
}
