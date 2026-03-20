package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceProperty extends PageProperty {

  private final String type = PagePropertyType.PLACE.type();

  @JsonProperty("place")
  private Place place = new Place();

  @Data
  public static class Place {

    /** The latitude. Example: 30.12 */
    @JsonProperty("lat")
    private Double lat;

    /** The longitude. Example: -60.72 */
    @JsonProperty("lon")
    private Double lon;

    /** A name for the location. Example: "Notion HQ" */
    @JsonProperty("name")
    private String name;

    /** An address for the location. Example: "" */
    @JsonProperty("address")
    private String address;

    /**
     * The corresponding ID value from AWS location provider. Only exposed for duplication or
     * echoing responses; will not be read. Example: "123"
     * //TODO only in reponse
     */
    @JsonProperty("aws_place_id")
    private String awsPlaceId;

    /**
     * The corresponding ID value from Google location provider. Only exposed for duplication or
     * echoing responses; will not be read. Example: "123"
     * //TODO only in reponse
     */
    @JsonProperty("google_place_id")
    private String googlePlaceId;
  }
}
