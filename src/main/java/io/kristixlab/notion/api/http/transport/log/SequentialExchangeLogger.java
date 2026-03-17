package io.kristixlab.notion.api.http.transport.log;

import io.kristixlab.notion.api.json.JsonConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialExchangeLogger implements ExchangeLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(SequentialExchangeLogger.class);
  protected final Path baseDir;
  protected final boolean onlyBody;

  public SequentialExchangeLogger(String baseDir, boolean bodyOnly) {
    this.baseDir = Paths.get(baseDir, "exchange-logs");
    this.onlyBody = bodyOnly;
    try {
      Files.createDirectories(this.baseDir);
    } catch (IOException e) {
      LOGGER.error("Failed to create exchange logs directory: {}", this.baseDir, e);
    }
  }

  // serviceName
  // requestHeaders
  public void logRequest(ExchangeContext context) {
    String logContent = extractRequestLog(context);
    String fileName = generateRqFileName(context);
    writeLogToFile(fileName, logContent);
  }

  private String extractRequestLog(ExchangeContext context) {
    if (onlyBody) {
      return (String) context.getOrDefault("requestBody", "[empty]");
    }

    ExchangeLog log = new ExchangeLog();
    log.setMethod((String) context.getOrDefault("method", "{method}"));
    log.setPath((String) context.getOrDefault("path", "{path}"));
    log.setHeaders((Map) context.getOrDefault("requestHeaders", "{}"));
    log.setRequestBody(context.getOrDefault("requestBody", new Object()));

    // TODO: is there a better way to secure sensitive data in headers? What is the best practice?
    log.getHeaders().computeIfPresent("Authorization", (k, v) -> "[redacted]");
    return JsonConverter.getInstance().toJson(log, true);
  }

  public void logResponse(ExchangeContext context) {
    String logContent = extractResponsetLog(context);
    String fileName = generateRsFileName(context);
    writeLogToFile(fileName, logContent);
  }

  private String extractResponsetLog(ExchangeContext context) {
    if (onlyBody) {
      return (String) context.getOrDefault("responseBody", "[empty]");
    }

    ExchangeLog log = new ExchangeLog();
    log.setMethod((String) context.getOrDefault("method", "{method}"));
    log.setPath((String) context.getOrDefault("path", "{path}"));
    log.setHeaders((Map) context.getOrDefault("responseHeaders", "{}"));
    log.setRequestBody(context.getOrDefault("responseBody", new Object()));

    return JsonConverter.getInstance().toJson(log, true);
  }

  private void writeLogToFile(String fileName, String content) {
    Path dir = getBaseDir();
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      LOGGER.error("Failed to create exchange logs directory: {}", dir, e);
    }
    Path logFilePath = dir.resolve(fileName);
    try {
      Files.writeString(logFilePath, content);
    } catch (IOException e) {
      LOGGER.error("Failed to write exchange log to file: {}", logFilePath, e);
    }
  }

  protected String generateRqFileName(ExchangeContext context) {
    return generateFileName(context, "rq");
  }

  protected String generateRsFileName(ExchangeContext context) {
    return generateFileName(context, "rs");
  }

  protected String generateFileName(ExchangeContext context, String postfix) {
    return String.format(
        "%d_%d_%s_%s.log",
        System.currentTimeMillis(),
        Thread.currentThread().getId(),
        context.getOrDefault("serviceName", "serviceName"),
        postfix);
  }

  protected Path getBaseDir() {
    return baseDir;
  }
}
