package ca.ulaval.glo4003.domain.notification;

public class Notification {
  public final String title;

  public final String summary;

  public final String message;

  public Notification(String title, String summary, String message) {
    this.title = title;
    this.summary = summary;
    this.message = message;
  }
}
