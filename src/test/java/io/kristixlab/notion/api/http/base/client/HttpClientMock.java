package io.kristixlab.notion.api.http.base.client;

/** A fake HttpClient that records the request it received and returns a fixed response. */
public class HttpClientMock implements HttpClient {
  HttpClient.HttpRequest lastRequest;
  final HttpClient.HttpResponse fixedResponse;

  HttpClientMock(HttpClient.HttpResponse fixedResponse) {
    this.fixedResponse = fixedResponse;
  }

  @Override
  public HttpClient.HttpResponse send(HttpClient.HttpRequest request) {
    this.lastRequest = request;
    return fixedResponse;
  }
}
