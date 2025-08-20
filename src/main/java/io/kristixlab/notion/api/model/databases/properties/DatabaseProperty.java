package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Base class for database property objects.
 * Database properties define the schema and configuration of database columns.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        defaultImpl = UnknownDatabaseProperty.class,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxDatabaseProperty.class, name = "checkbox"),
        @JsonSubTypes.Type(value = CreatedByDatabaseProperty.class, name = "created_by"),
        @JsonSubTypes.Type(value = CreatedTimeDatabaseProperty.class, name = "created_time"),
        @JsonSubTypes.Type(value = DateDatabaseProperty.class, name = "date"),
        @JsonSubTypes.Type(value = EmailDatabaseProperty.class, name = "email"),
        @JsonSubTypes.Type(value = FilesDatabaseProperty.class, name = "files"),
        @JsonSubTypes.Type(value = FormulaDatabaseProperty.class, name = "formula"),
        @JsonSubTypes.Type(value = LastEditedByDatabaseProperty.class, name = "last_edited_by"),
        @JsonSubTypes.Type(value = LastEditedTimeDatabaseProperty.class, name = "last_edited_time"),
        @JsonSubTypes.Type(value = MultiSelectDatabaseProperty.class, name = "multi_select"),
        @JsonSubTypes.Type(value = NumberDatabaseProperty.class, name = "number"),
        @JsonSubTypes.Type(value = PeopleDatabaseProperty.class, name = "people"),
        @JsonSubTypes.Type(value = PhoneNumberDatabaseProperty.class, name = "phone_number"),
        @JsonSubTypes.Type(value = RelationDatabaseProperty.class, name = "relation"),
        @JsonSubTypes.Type(value = RichTextDatabaseProperty.class, name = "rich_text"),
        @JsonSubTypes.Type(value = RollupDatabaseProperty.class, name = "rollup"),
        @JsonSubTypes.Type(value = SelectDatabaseProperty.class, name = "select"),
        @JsonSubTypes.Type(value = StatusDatabaseProperty.class, name = "status"),
        @JsonSubTypes.Type(value = TitleDatabaseProperty.class, name = "title"),
        @JsonSubTypes.Type(value = UniqueIdDatabaseProperty.class, name = "unique_id"),
        @JsonSubTypes.Type(value = UrlDatabaseProperty.class, name = "url"),
        @JsonSubTypes.Type(value = VerificationDatabaseProperty.class, name = "verification")
})
@Data
public abstract class DatabaseProperty {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    // Type conversion methods similar to PageProperty
    public CheckboxDatabaseProperty asCheckbox() {
        return (CheckboxDatabaseProperty) this;
    }

    public CreatedByDatabaseProperty asCreatedBy() {
        return (CreatedByDatabaseProperty) this;
    }

    public CreatedTimeDatabaseProperty asCreatedTime() {
        return (CreatedTimeDatabaseProperty) this;
    }

    public DateDatabaseProperty asDate() {
        return (DateDatabaseProperty) this;
    }

    public EmailDatabaseProperty asEmail() {
        return (EmailDatabaseProperty) this;
    }

    public FilesDatabaseProperty asFiles() {
        return (FilesDatabaseProperty) this;
    }

    public FormulaDatabaseProperty asFormula() {
        return (FormulaDatabaseProperty) this;
    }

    public LastEditedByDatabaseProperty asLastEditedBy() {
        return (LastEditedByDatabaseProperty) this;
    }

    public LastEditedTimeDatabaseProperty asLastEditedTime() {
        return (LastEditedTimeDatabaseProperty) this;
    }

    public MultiSelectDatabaseProperty asMultiSelect() {
        return (MultiSelectDatabaseProperty) this;
    }

    public NumberDatabaseProperty asNumber() {
        return (NumberDatabaseProperty) this;
    }

    public PeopleDatabaseProperty asPeople() {
        return (PeopleDatabaseProperty) this;
    }

    public PhoneNumberDatabaseProperty asPhoneNumber() {
        return (PhoneNumberDatabaseProperty) this;
    }

    public RelationDatabaseProperty asRelation() {
        return (RelationDatabaseProperty) this;
    }

    public RichTextDatabaseProperty asRichText() {
        return (RichTextDatabaseProperty) this;
    }

    public RollupDatabaseProperty asRollup() {
        return (RollupDatabaseProperty) this;
    }

    public SelectDatabaseProperty asSelect() {
        return (SelectDatabaseProperty) this;
    }

    public StatusDatabaseProperty asStatus() {
        return (StatusDatabaseProperty) this;
    }

    public TitleDatabaseProperty asTitle() {
        return (TitleDatabaseProperty) this;
    }

    public UniqueIdDatabaseProperty asUniqueId() {
        return (UniqueIdDatabaseProperty) this;
    }

    public UrlDatabaseProperty asUrl() {
        return (UrlDatabaseProperty) this;
    }

    public VerificationDatabaseProperty asVerification() {
        return (VerificationDatabaseProperty) this;
    }

    // Type checking methods
    @JsonIgnore
    public boolean isCheckbox() {
        return "checkbox".equals(type);
    }

    @JsonIgnore
    public boolean isCreatedBy() {
        return "created_by".equals(type);
    }

    @JsonIgnore
    public boolean isCreatedTime() {
        return "created_time".equals(type);
    }

    @JsonIgnore
    public boolean isDate() {
        return "date".equals(type);
    }

    @JsonIgnore
    public boolean isEmail() {
        return "email".equals(type);
    }

    @JsonIgnore
    public boolean isFiles() {
        return "files".equals(type);
    }

    @JsonIgnore
    public boolean isFormula() {
        return "formula".equals(type);
    }

    @JsonIgnore
    public boolean isLastEditedBy() {
        return "last_edited_by".equals(type);
    }

    @JsonIgnore
    public boolean isLastEditedTime() {
        return "last_edited_time".equals(type);
    }

    @JsonIgnore
    public boolean isMultiSelect() {
        return "multi_select".equals(type);
    }

    @JsonIgnore
    public boolean isNumber() {
        return "number".equals(type);
    }

    @JsonIgnore
    public boolean isPeople() {
        return "people".equals(type);
    }

    @JsonIgnore
    public boolean isPhoneNumber() {
        return "phone_number".equals(type);
    }

    @JsonIgnore
    public boolean isRelation() {
        return "relation".equals(type);
    }

    @JsonIgnore
    public boolean isRichText() {
        return "rich_text".equals(type);
    }

    @JsonIgnore
    public boolean isRollup() {
        return "rollup".equals(type);
    }

    @JsonIgnore
    public boolean isSelect() {
        return "select".equals(type);
    }

    @JsonIgnore
    public boolean isStatus() {
        return "status".equals(type);
    }

    @JsonIgnore
    public boolean isTitle() {
        return "title".equals(type);
    }

    @JsonIgnore
    public boolean isUniqueId() {
        return "unique_id".equals(type);
    }

    @JsonIgnore
    public boolean isUrl() {
        return "url".equals(type);
    }

    @JsonIgnore
    public boolean isVerification() {
        return "verification".equals(type);
    }
}
