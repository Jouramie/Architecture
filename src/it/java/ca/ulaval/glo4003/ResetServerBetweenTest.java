package ca.ulaval.glo4003;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ResetServerBetweenTest implements TestRule {

    private static final Integer TEN_MILLIS = 10;

    private Thread thread;

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                startServer();
                try{
                    statement.evaluate();
                } finally {
                    stopServer();
                }
            }
        };
    }

    private void startServer() throws Exception{
        thread = new Thread(this::executeMain);
        thread.setDaemon(true);
        thread.start();
        waitForServerToBeUp();
    }

    private void executeMain() {
        try {
            InvestULMain.main(new String[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForServerToBeUp() throws Exception{
        while(!InvestULMain.isStarted()) {
            Thread.sleep(TEN_MILLIS);
        }
    }

    private void stopServer() throws Exception{
        InvestULMain.stop();
        thread.join();
    }
}
