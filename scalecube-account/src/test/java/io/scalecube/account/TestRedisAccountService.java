package io.scalecube.account;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.Token;
import junit.framework.TestCase;


public class TestRedisAccountService extends TestCase {
  String googleToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM1MWIwZjZjNDM0NDgwOGQ1NzBlNzkyMTVjYWI1NDk5NzI1ZTM2ZjIifQ.eyJhenAiOiIxMzIwNDQ0OTAzNDItdmo0OGRlY2xzYWowazJmNzJobjB0Nzlkc29pZDVrdmMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxMzIwNDQ0OTAzNDItdmo0OGRlY2xzYWowazJmNzJobjB0Nzlkc29pZDVrdmMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDM1MDExMTIxODk4NDk0MzI4MTciLCJoZCI6Im1hcmtldHNwdWxzZS5jb20iLCJlbWFpbCI6InJvbmVuQG1hcmtldHNwdWxzZS5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IlI1SWZleU5HMlNQdjlzSWlweTFjWkEiLCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNDk4MTg1OTQ1LCJleHAiOjE0OTgxODk1NDUsIm5hbWUiOiJSb25lbiBOYWNobWlhcyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vLXpXb2NuWGNMb3NrL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FJNnlHWHlfQ3AzNDRQZXp2dkszX3ZEVGhaVG5jdmhZWkEvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IlJvbmVuIiwiZmFtaWx5X25hbWUiOiJOYWNobWlhcyIsImxvY2FsZSI6ImVuIn0.a4b4cz9ySvx1GOTmkmK1Ws76oCwO2X2QTRGoFvxs6tVLID_sYpR7uRStzXEHI9bh8fY3_Ky86OAfLlLmpjEv-40y84RXWPMEEl8MO75Z5bVPpCVUpzfgy9k6uMt4QFoEtyubOkuFSxcpYwUrdABUXC5RaHsKupfC9hao0QgwY0PfyDOVGV5BNp4LDrQKazXkFt3E7I33-MeU0diJiKHy6rwHXy2uamWzcsYF-30jWAR6alN7LsFzwW4dnbHKAH9GbrJqnLeskJs4ZDRwsvViTGbDthHCbDa3kqSUXCmnfPstZd2wRPl-Nd8vipk2c4ZduluMFJ8S2oCgTrWDy1Dbbg";

  final AccountService account;
  
  public TestRedisAccountService() throws Exception{
    account = new RedisAccountService();
  }
 
  public void testCreateOrganization() throws Exception {
    
    Token token = new Token("google", googleToken);
    account.createOrganization(new CreateOrganizationRequest("myTestOrg5", token,"email"))
        .whenComplete((success, error) -> {
          if (error == null) {
            System.out.println(success);
          }
        });
  }

  public void testGetOrganizationDetails() throws Exception {
    Token token = new Token("google", googleToken);
    account.getUserOrganizationsMembership(new GetMembershipRequest(token))
        .whenComplete((success, error) -> {
          if (error == null) {
            System.out.println(success);
          }
        });
  }

}
