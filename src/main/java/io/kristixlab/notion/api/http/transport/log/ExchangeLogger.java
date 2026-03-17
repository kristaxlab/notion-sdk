package io.kristixlab.notion.api.http.transport.log;

public interface ExchangeLogger {

  void logRequest(ExchangeContext context);

  void logResponse(ExchangeContext context);
}
