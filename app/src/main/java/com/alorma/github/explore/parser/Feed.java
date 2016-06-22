package com.alorma.github.explore.parser;

import java.util.Date;
import java.util.List;

/**
 * A web feed that contains the items.
 */
public interface Feed extends Element {

  /**
   * Returns the feed type.
   */
  FeedType getType();

  /**
   * Convenience method to retrieve the feed title.
   */
  String getTitle();

  /**
   * Convenience method to retrieve the feed link.
   */
  String getLink();

  /**
   * Convenience method to retrieve the feed description.
   */
  String getDescription();

  /**
   * Convenience method to retrieve the language.
   */
  String getLanguage();

  /**
   * Convenience method to retrieve the copyright.
   */
  String getCopyright();

  /**
   * Convenience method to retrieve the published date.
   */
  Date getPubDate();

  /**
   * Convenience method to retrieve a list of categories.
   */
  List<String> getCategories();

  /**
   * Returns a list of Items in the feed.
   */
  List<Item> getItemList();
}
