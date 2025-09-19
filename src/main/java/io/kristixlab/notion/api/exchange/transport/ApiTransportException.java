package io.kristixlab.notion.api.exchange.transport;

public class ApiTransportException extends RuntimeException {

  public ApiTransportException(Exception e) {
    super(e);
  }

  public ApiTransportException(String message, Exception e) {
    super(message, e);
  }
}
