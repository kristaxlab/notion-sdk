package io.kristixlab.notion.api.util;

import io.kristixlab.notion.api.model.pages.properties.*;

public class PagePropertyUtil {

  public static boolean isNumber(PageProperty property) {
    return PagePropertyType.NUMBER.type().equals(property.getType());
  }

  public static NumberProperty asNumber(PageProperty property) {
    return (NumberProperty) property;
  }

  public static boolean isTitle(PageProperty property) {
    return PagePropertyType.TITLE.type().equals(property.getType());
  }

  public static TitleProperty asTitle(PageProperty property) {
    return (TitleProperty) property;
  }

  public static boolean isRichText(PageProperty property) {
    return PagePropertyType.RICH_TEXT.type().equals(property.getType());
  }

  public static RichTextProperty asRichText(PageProperty property) {
    return (RichTextProperty) property;
  }

  public static boolean isSelect(PageProperty property) {
    return PagePropertyType.SELECT.type().equals(property.getType());
  }

  public static SelectProperty asSelect(PageProperty property) {
    return (SelectProperty) property;
  }

  public static boolean isMultiSelect(PageProperty property) {
    return PagePropertyType.MULTI_SELECT.type().equals(property.getType());
  }

  public static MultiSelectProperty asMultiSelect(PageProperty property) {
    return (MultiSelectProperty) property;
  }

  public static boolean isCheckbox(PageProperty property) {
    return PagePropertyType.CHECKBOX.type().equals(property.getType());
  }

  public static CheckboxProperty asCheckbox(PageProperty property) {
    return (CheckboxProperty) property;
  }

  public static boolean isCreatedTime(PageProperty property) {
    return PagePropertyType.CREATED_TIME.type().equals(property.getType());
  }

  public static CreatedTimeProperty asCreatedTime(PageProperty property) {
    return (CreatedTimeProperty) property;
  }

  public static boolean isLastEditedTime(PageProperty property) {
    return PagePropertyType.LAST_EDITED_TIME.type().equals(property.getType());
  }

  public static LastEditedTimeProperty asLastEditedTime(PageProperty property) {
    return (LastEditedTimeProperty) property;
  }

  public static boolean isCreatedBy(PageProperty property) {
    return PagePropertyType.CREATED_BY.type().equals(property.getType());
  }

  public static CreatedByProperty asCreatedBy(PageProperty property) {
    return (CreatedByProperty) property;
  }

  public static boolean isLastEditedBy(PageProperty property) {
    return PagePropertyType.LAST_EDITED_BY.type().equals(property.getType());
  }

  public static LastEditedByProperty asLastEditedBy(PageProperty property) {
    return (LastEditedByProperty) property;
  }

  public static boolean isDate(PageProperty property) {
    return PagePropertyType.DATE.type().equals(property.getType());
  }

  public static DateProperty asDate(PageProperty property) {
    return (DateProperty) property;
  }

  public static boolean isEmail(PageProperty property) {
    return PagePropertyType.EMAIL.type().equals(property.getType());
  }

  public static EmailProperty asEmail(PageProperty property) {
    return (EmailProperty) property;
  }

  public static boolean isPhoneNumber(PageProperty property) {
    return PagePropertyType.PHONE_NUMBER.type().equals(property.getType());
  }

  public static PhoneNumberProperty asPhoneNumber(PageProperty property) {
    return (PhoneNumberProperty) property;
  }

  public static boolean isFiles(PageProperty property) {
    return PagePropertyType.FILES.type().equals(property.getType());
  }

  public static FilesProperty asFiles(PageProperty property) {
    return (FilesProperty) property;
  }

  public static boolean isPeople(PageProperty property) {
    return PagePropertyType.PEOPLE.type().equals(property.getType());
  }

  public static PeopleProperty asPeople(PageProperty property) {
    return (PeopleProperty) property;
  }

  public static boolean isRelation(PageProperty property) {
    return PagePropertyType.RELATION.type().equals(property.getType());
  }

  public static RelationProperty asRelation(PageProperty property) {
    return (RelationProperty) property;
  }

  public static boolean isRollup(PageProperty property) {
    return PagePropertyType.ROLLUP.type().equals(property.getType());
  }

  public static RollupProperty asRollup(PageProperty property) {
    return (RollupProperty) property;
  }

  public static boolean isFormula(PageProperty property) {
    return PagePropertyType.FORMULA.type().equals(property.getType());
  }

  public static FormulaProperty asFormula(PageProperty property) {
    return (FormulaProperty) property;
  }

  public static boolean isUrl(PageProperty property) {
    return PagePropertyType.URL.type().equals(property.getType());
  }

  public static UrlProperty asUrl(PageProperty property) {
    return (UrlProperty) property;
  }

  public static boolean isStatus(PageProperty property) {
    return PagePropertyType.STATUS.type().equals(property.getType());
  }

  public static StatusProperty asStatus(PageProperty property) {
    return (StatusProperty) property;
  }

  public static boolean isButton(PageProperty property) {
    return PagePropertyType.BUTTON.type().equals(property.getType());
  }

  public static ButtonProperty asButton(PageProperty property) {
    return (ButtonProperty) property;
  }

  public static boolean isPlace(PageProperty property) {
    return PagePropertyType.PLACE.type().equals(property.getType());
  }

  public static PlaceProperty asPlace(PageProperty property) {
    return (PlaceProperty) property;
  }

  public static boolean isVerification(PageProperty property) {
    return PagePropertyType.VERIFICATION.type().equals(property.getType());
  }

  public static VerificationProperty asVerification(PageProperty property) {
    return (VerificationProperty) property;
  }

  public static boolean isUniqueId(PageProperty property) {
    return PagePropertyType.UNIQUE_ID.type().equals(property.getType());
  }

  public static UniqueIdProperty asUniqueId(PageProperty property) {
    return (UniqueIdProperty) property;
  }

  public static boolean isPropertyItem(PageProperty property) {
    return PagePropertyType.PROPERTY_ITEM.type().equals(property.getType());
  }

  public static PropertyItem asPropertyItem(PageProperty property) {
    return (PropertyItem) property;
  }
}
