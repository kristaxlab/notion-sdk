package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceProperty extends PageProperty {

  private final String type = PagePropertyType.PLACE.type();

  private Place place = new Place();

  @Getter
  @Setter
  public static class Place {

    /** The latitude. Example: 30.12 */
    private Double lat;

    /** The longitude. Example: -60.72 */
    private Double lon;

    /** A name for the location. Example: "Notion HQ" */
    private String name;

    /** An address for the location. Example: "" */
    private String address;

    /**
     * The corresponding ID value from AWS location provider. Only exposed for duplication or
     * echoing responses; will not be read. Example: "123" //TODO only in reponse
     */
    private String awsPlaceId;

    /**
     * The corresponding ID value from Google location provider. Only exposed for duplication or
     * echoing responses; will not be read. Example: "123" //TODO only in reponse
     */
    private String googlePlaceId;
  }
}
