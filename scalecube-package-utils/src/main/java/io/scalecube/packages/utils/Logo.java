package io.scalecube.packages.utils;

import java.util.HashMap;
import java.util.Map;

public class Logo {

  public static class LogoHeader {
    private static final String space = "        ";
    private String value;

    public LogoHeader(String value) {
      this.value = space + value;
    }

    public String value() {
      return this.value;
    }
  }

  public static class Builder {
    private int index = 0;
    private int startAt = 5;
    private Map<Integer, LogoHeader> headers = new HashMap<>();

    public Builder ip(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("IP Address: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder tagVersion(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("ScaleCube #1 is Running.".replaceAll("#1", value)));
      return this;
    }

    public Builder group(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("Group: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder artifact(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("Artifact: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder port(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("Port: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder osType(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("OS: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder pid(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("PID: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder javaVersion(String value) {
      headers.put(startAt + headers.size() + 1, new LogoHeader("Java: #1".replaceAll("#1", value)));
      return this;
    }

    public Builder header(String header) {
      headers.put(startAt + headers.size() + 1, new LogoHeader(header));
      return this;
    }

    public Builder website() {
      headers.put(startAt + headers.size() + 2, new LogoHeader("      http://scalecube.io"));
      return this;
    }



    /**
     * draw the scalecube logo.
     */
    public void draw() {
      pln("                         .,,,,,,                          ");
      pln("                       .,,,,,,,,,,,,/                     ");
      pln("                  .,,,,,,,,,,,,,,,,,,,.                   ");
      pln("               .,,,,,,,,,,,,,,,,,,,,,,,,,,,.              ");
      pln("            .,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.           ");
      pln("         ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,        ");
      pln("     ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,     ");
      pln("     *  ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,*       ");
      pln("     ***** /,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,/  ....    ");
      pln("     ******** /*,,,,,,,,,,,,,,,,,,,,,,,,,,,,/  .......    ");
      pln("     ***********, /,,,,,,,,,,,,,,,,,,,,,*  ...........    ");
      pln("     ***************. *,,,,,,,,,,,,,*/ ...............    ");
      pln("     ******************, /*,,,,,,/  ..................    ");
      pln("     ********************** /*/  .....................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("     ************************ ........................    ");
      pln("         ******************** ....................        ");
      pln("            ***************** .................           ");
      pln("                ************* .............               ");
      pln("                   ********** ..........                  ");
      pln("                       ****** ......                      ");
      pln("                          *** ...                         ");
    }

    private void pln(String line) {
      LogoHeader logoHeader = headers.get(index);
      if (logoHeader != null) {
        System.out.println(line + logoHeader.value());
      } else {
        System.out.println(line);
      }
      index++;
    }


  }

  public static Builder builder() {
    return new Builder();
  }
}

