package com.walterjwhite.calendar.api.repository;

import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.datastore.query.AbstractQuery;
import java.util.List;
import lombok.Getter;

@Getter
public class FindCalendarEventsByTagQuery extends AbstractQuery<CalendarEvent, List> {
  protected final Tag tag;

  public FindCalendarEventsByTagQuery(Tag tag) {
    super(0, -1, CalendarEvent.class, List.class, false);
    this.tag = tag;
  }
}
