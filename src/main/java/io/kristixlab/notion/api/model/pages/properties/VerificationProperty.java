package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.DateData;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationProperty extends PageProperty {
    private final String type = "verification";
    @JsonProperty("verification")
    private VerificationValue verification;

    @Data
    public static class VerificationValue {
        private String state;
        @JsonProperty("verified_by")
        private User verifiedBy;
        @JsonProperty("date")
        private DateData date;
    }
}
