package io.kristixlab.notion.api.http.transport.exception;

public class HttpTransportException extends RuntimeException {

  public HttpTransportException(Exception e) {
    super(e);
  }

  public HttpTransportException(String message, Exception e) {
    super(message, e);
  }
}
