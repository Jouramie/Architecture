package ca.ulaval.glo4003;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class StartServerRule implements TestRule {

    @Override
    public Statement apply(Statement statement, Description description) {
        Thread thread = new Thread(() -> {
          try {
            InvestULMain.main(new String[] {});
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        thread.setDaemon(true);
        thread.start();
        return statement;
    }
}
