package io.kristixlab.notion.api.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseNotionResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("avatar_url")
  private String avatarUrl;

  @JsonProperty("type")
  private String type;

  @JsonProperty("person")
  private Person person;

  @JsonProperty("bot")
  private Bot bot;
}
