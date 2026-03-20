package io.kristixlab.notion.api.model.datasources.properties.helper;

import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A step in a fluent request-builder chain that configures a data source property schema.
 *
 * <p>Instances are returned by {@code propertiesBuilder()} on request-builder classes such as
 * {@code CreateDatabaseParams.Builder} and {@code CreateDataSourceParams.Builder}. Every property
 * method (defined in {@link AbstractDataSourceSchemaBuilder}) returns {@code PropertiesStep<P>},
 * keeping the chain unbroken. Call {@link #buildProperties()} to finalise the schema and return to
 * the parent builder.
 *
 * <pre>{@code
 * CreateDatabaseParams params = CreateDatabaseParams.builder()
 *     .parentPage(pageId)
 *     .title("My DB")
 *     .propertiesBuilder()                        // → PropertiesStep<Builder>
 *         .title("Name")                          // → PropertiesStep<Builder>
 *         .number("Price", NumberFormatType.EURO) // → PropertiesStep<Builder>
 *         .select("Status", "Active", "Archived") // → PropertiesStep<Builder>
 *     .buildProperties()                          // → Builder
 *     .build();                                   // → CreateDatabaseParams
 * }</pre>
 *
 * @param <PARENT> the type of the parent builder to return to after {@link #buildProperties()}
 * @see DataSourceSchemaBuilder
 * @see AbstractDataSourceSchemaBuilder
 */
public final class PropertiesStep<PARENT>
    extends AbstractDataSourceSchemaBuilder<PropertiesStep<PARENT>> {

  private final PARENT parent;
  private final Consumer<Map<String, DataSourcePropertySchemaParams>> setter;

  /**
   * Creates a new step builder.
   *
   * @param parent the parent builder to return to via {@link #buildProperties()}
   * @param setter a consumer that applies the finished schema map to the parent builder
   */
  public PropertiesStep(PARENT parent, Consumer<Map<String, DataSourcePropertySchemaParams>> setter) {
    this.parent = parent;
    this.setter = setter;
  }

  /**
   * Finalises the property schema and returns to the parent builder.
   *
   * <p>Equivalent to calling {@link AbstractDataSourceSchemaBuilder#build()} and passing the
   * result to the parent's {@code properties(Map)} method, but without breaking the chain.
   *
   * @return the parent builder for continued configuration
   */
  public PARENT buildProperties() {
    setter.accept(build());
    return parent;
  }
}


