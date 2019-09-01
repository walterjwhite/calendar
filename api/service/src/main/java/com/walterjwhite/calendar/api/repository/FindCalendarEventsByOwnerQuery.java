package com.walterjwhite.calendar.api.repository;

import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.query.AbstractQuery;
import com.walterjwhite.person.api.model.Person;
import java.util.List;
import lombok.Getter;

@Getter
public class FindCalendarEventsByOwnerQuery extends AbstractQuery<CalendarEvent, List> {
  protected final Person owner;

  public FindCalendarEventsByOwnerQuery(Person owner) {
    super(0, -1, CalendarEvent.class, List.class, false);
    this.owner = owner;
  }
}
