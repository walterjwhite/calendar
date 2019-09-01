package com.walterjwhite.calendar.api.repository;

import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.query.AbstractQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class FindCalendarEventsWithinDateRange extends AbstractQuery<CalendarEvent, List> {
  protected final LocalDateTime startRange;
  protected final LocalDateTime endRange;

  public FindCalendarEventsWithinDateRange(LocalDateTime startRange, LocalDateTime endRange) {
    super(0, -1, CalendarEvent.class, List.class, false);
    this.startRange = startRange;
    this.endRange = endRange;
  }
}
