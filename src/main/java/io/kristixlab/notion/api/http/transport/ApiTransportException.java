package io.kristixlab.notion.api.http.transport;

public class ApiTransportException extends RuntimeException {

  public ApiTransportException(Exception e) {
    super(e);
  }

  public ApiTransportException(String message, Exception e) {
    super(message, e);
  }
}
