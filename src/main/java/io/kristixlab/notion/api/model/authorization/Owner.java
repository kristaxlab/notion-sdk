package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;

/**
 * Represents the owner of a workspace in the OAuth token response.
 */
@Data
public class Owner {

    /**
     * The type of owner, typically "user".
     */
    @JsonProperty("type")
    private String type;

    /**
     * The user information when the owner is a user.
     */
    @JsonProperty("user")
    private User user;
}
