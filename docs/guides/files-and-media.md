# Files and Media

The SDK supports three upload modes: **single-part** (small files in one request), **multi-part** (large files split into chunks), and **external URL** (import a publicly accessible file by URL).

---

## Single-part upload

Use this for files that fit in a single request.

```java
NotionClient client = NotionClient.forToken("secret_abc123...");

// 1. Create the upload
FileUpload upload = client.fileUploads().createFileUpload(
    FileUploadCreateParams.singlePart("report.pdf")
);

// 2. Send file content
FileUpload sent = client.fileUploads().sendFileContent(
    upload.getId(),
    FileUploadSendParams.of(new File("report.pdf"), "application/pdf", "report.pdf")
);
```

You can also supply an `InputStream` or a `byte[]` instead of a `File`:

```java
// From InputStream
FileUploadSendParams.of(inputStream, "image/png", "photo.png")

// From byte array
FileUploadSendParams.of(bytes, "image/png", "photo.png")
```

---

## Multi-part upload

Use this for large files that must be split into numbered parts (1–1,000).

```java
// 1. Create the upload, declaring the number of parts
FileUpload upload = client.fileUploads().createFileUpload(
    FileUploadCreateParams.multiPart("large-video.mp4", 3)
);

String id = upload.getId();

// 2. Send each part in order
client.fileUploads().sendFileContent(id,
    FileUploadSendParams.builder()
        .file(new File("part1.bin")).contentType("video/mp4").fileName("large-video.mp4")
        .partNumber(1).build());

client.fileUploads().sendFileContent(id,
    FileUploadSendParams.builder()
        .file(new File("part2.bin")).contentType("video/mp4").fileName("large-video.mp4")
        .partNumber(2).build());

client.fileUploads().sendFileContent(id,
    FileUploadSendParams.builder()
        .file(new File("part3.bin")).contentType("video/mp4").fileName("large-video.mp4")
        .partNumber(3).build());

// 3. Finalize
FileUpload completed = client.fileUploads().completeFileUpload(id);
```

---

## External URL upload

Use this to import a file directly from a public HTTPS URL without sending the bytes yourself.

```java
FileUpload upload = client.fileUploads().createFileUpload(
    FileUploadCreateParams.external("logo.png", "https://example.com/assets/logo.png")
);
```

Notion fetches the file in the background. Poll until the status is no longer `pending`:

```java
FileUpload result = client.fileUploads().retrieveFileUpload(upload.getId());
System.out.println(result.getStatus()); // "uploaded" once complete
```

---

## Listing uploads

```java
// All uploads
FileUploadList all = client.fileUploads().listFileUploads();

// Filter by status: pending | uploaded | expired | failed
FileUploadList pending = client.fileUploads().listFileUploads("pending");

// With pagination
FileUploadList page = client.fileUploads().listFileUploads("uploaded", cursor, 10);
```