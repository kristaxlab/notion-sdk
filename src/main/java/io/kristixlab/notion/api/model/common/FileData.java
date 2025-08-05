package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FileData {

    @JsonProperty("type")
    private String type;

    @JsonProperty("external")
    private FileData.External external;

    @JsonProperty("file")
    private FileData.File file;

    @JsonProperty("file_upload")
    private FileData.FileUpload fileUpload;

    @JsonProperty("caption")
    private List<RichText> caption;

    @JsonProperty("name")
    private String name;

    @Data
    public static class External {
        @JsonProperty("url")
        private String url;
    }

    @Data
    public static class File {

        @JsonProperty("url")
        private String url;

        @JsonProperty("expiry_time")
        private String expiryTime;
    }

    @Data
    public static class FileUpload {

        @JsonProperty("id")
        private String id;
    }
}
