package juzu.impl.standalone;

import juzu.impl.common.Tools;
import juzu.test.protocol.standalone.AbstractStandaloneTestCase;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class ResponseResourceTestCase extends AbstractStandaloneTestCase {

  @Drone
  WebDriver driver;

  @Test
  public void testPathParam() throws Exception {
    assertDeploy("standalone", "response", "header", "resource");
    driver.get(deploymentURL.toString());
    WebElement trigger = driver.findElement(By.tagName("body"));
    URL url = new URL(trigger.getText());
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.connect();
    Map<String, String> headers = Tools.responseHeaders(conn);
    assertTrue(headers.containsKey("juu"));
    assertEquals("juu_value", headers.get("juu"));
    assertUndeploy();
  }
}
