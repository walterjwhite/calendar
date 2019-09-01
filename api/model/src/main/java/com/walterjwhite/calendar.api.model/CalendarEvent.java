package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.person.api.model.Person;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable

@Entity
public class CalendarEvent extends AbstractEntity {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false, unique = true, updatable = false)
  protected String serverId;

  @EqualsAndHashCode.Exclude
  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  protected Calendar calendar;

  @EqualsAndHashCode.Exclude @Column protected LocalDateTime start;
  @EqualsAndHashCode.Exclude @Column protected LocalDateTime end;

  @EqualsAndHashCode.Exclude @Column protected String subject;
  @EqualsAndHashCode.Exclude @Column protected String description;
  @EqualsAndHashCode.Exclude @Column protected String location;

  @EqualsAndHashCode.Exclude @ManyToMany @JoinTable protected Set<Person> toRecipients;
  @EqualsAndHashCode.Exclude @ManyToMany @JoinTable protected Set<Person> ccRecipients;
  @EqualsAndHashCode.Exclude @ManyToMany @JoinTable protected Set<Person> bccRecipients;

  @EqualsAndHashCode.Exclude @ManyToMany @JoinTable protected Set<CalendarAttachment> files;

  public CalendarEvent(
      Calendar calendar,
      LocalDateTime start,
      LocalDateTime end,
      String subject,
      String description,
      String location,
      Set<Person> toRecipients,
      Set<Person> ccRecipients,
      Set<Person> bccRecipients) {
    this();

    this.calendar = calendar;
    this.start = start;
    this.end = end;
    this.subject = subject;
    this.description = description;
    this.location = location;

    if (toRecipients != null && !toRecipients.isEmpty()) this.toRecipients.addAll(toRecipients);
    if (ccRecipients != null && !ccRecipients.isEmpty()) this.ccRecipients.addAll(ccRecipients);
    if (bccRecipients != null && !bccRecipients.isEmpty()) this.bccRecipients.addAll(bccRecipients);
    // if (files != null && !files.isEmpty()) this.files.addAll(files);
  }

  public CalendarEvent() {
    super();

    toRecipients = new HashSet<>();
    ccRecipients = new HashSet<>();
    bccRecipients = new HashSet<>();
    files = new HashSet<>();
  }
}
