package com.walterjwhite.calendar.api.repository;

import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.query.AbstractQuery;
import java.util.List;
import lombok.Getter;

@Getter
public class FindCalendarEventsBySubjectContainingQuery extends AbstractQuery<CalendarEvent, List> {
  protected final String subject;

  public FindCalendarEventsBySubjectContainingQuery(String subject) {
    super(0, -1, CalendarEvent.class, List.class, false);
    this.subject = subject;
  }
}
