package ca.ulaval.glo4003;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.context.ProductionContext;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ResetServerBetweenTest implements TestRule {
  private static final Integer TEN_MILLIS = 10;
  private final AbstractContext context;
  private Thread thread;

  public ResetServerBetweenTest() {
    context = new ProductionContext();
  }

  public ResetServerBetweenTest(AbstractContext context) {
    this.context = context;
  }

  @Override
  public Statement apply(Statement statement, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        startServer();
        try {
          statement.evaluate();
        } finally {
          stopServer();
        }
      }
    };
  }

  private void startServer() throws Exception {
    thread = new Thread(this::executeMain);
    thread.start();
    waitForServerToBeUp();
  }

  private void executeMain() {
    try {
      ServiceLocator.INSTANCE.reset();
      InvestULMain.startServer(context);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void waitForServerToBeUp() throws Exception {
    while (!InvestULMain.isStarted()) {
      Thread.sleep(TEN_MILLIS);
    }
  }

  private void stopServer() throws Exception {
    InvestULMain.stop();
    thread.join();
  }
}
