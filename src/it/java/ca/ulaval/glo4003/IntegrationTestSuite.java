package ca.ulaval.glo4003;

import ca.ulaval.glo4003.ws.api.PingResourceIT;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PingResourceIT.class})
public class IntegrationTestSuite {

    @BeforeClass
    public static void startServer() {
        Thread thread = new Thread(() -> {
          try {
            InvestULMain.main(new String[] {});
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
