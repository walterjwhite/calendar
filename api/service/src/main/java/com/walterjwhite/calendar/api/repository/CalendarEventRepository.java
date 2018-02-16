package com.walterjwhite.calendar.api.repository;

import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.person.api.model.Person;
import java.time.LocalDateTime;
import java.util.Set;

public interface CalendarEventRepository /*extends AbstractEntityJPARepository<CalendarEvent>*/ {

  Set<CalendarEvent> findByFrom(final Person person);

  Set<CalendarEvent> findByTagsContaining(final Tag tag);

  Set<CalendarEvent> findBySubjectContaining(final String subjectExcerpt);

  Set<CalendarEvent> findBySentDateBetween(
      final LocalDateTime startRange, final LocalDateTime endRange);
}
