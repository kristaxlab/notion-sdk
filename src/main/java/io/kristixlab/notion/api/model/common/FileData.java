package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.NotionText;
import io.kristixlab.notion.api.model.helper.NotionTextBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {

  /** "file" (only in responses), "external", "file_upload" */
  private String type;

  private List<RichText> caption;

  private ExternalFile external;

  /** only for requests */
  private FileUploadRef fileUpload;

  /** only may return in response */
  private File file;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private ExternalFile external;
    private FileUploadRef fileUpload;
    private List<RichText> caption = new ArrayList<>();

    public Builder externalUrl(String url) {
      this.external = new ExternalFile();
      this.external.setUrl(url);
      return this;
    }

    public Builder fileUpload(String fileUploadId) {
      this.fileUpload = new FileUploadRef();
      this.fileUpload.setId(fileUploadId);
      return this;
    }

    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
      return this;
    }

    public FileData build() {
      FileData fileData = new FileData();
      if (external == null && fileUpload == null) {
        throw new IllegalStateException("Either external or fileUpload must be set");
      }

      if (external != null && fileUpload != null) {
        throw new IllegalStateException("External and fileUpload cannot both be set");
      }

      if (external != null) {
        fileData.setType("external");
      } else {
        fileData.setType("file_upload");
      }
      fileData.setExternal(external);
      fileData.setFileUpload(fileUpload);
      if (!caption.isEmpty()) {
        fileData.setCaption(new ArrayList<>(caption));
      }
      return fileData;
    }
  }
}
