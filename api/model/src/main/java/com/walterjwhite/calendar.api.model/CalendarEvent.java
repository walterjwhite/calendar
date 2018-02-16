package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.person.api.model.Person;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class CalendarEvent extends AbstractEntity {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false, unique = true, updatable = false)
  protected String serverId;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  protected Calendar calendar;

  @Column protected LocalDateTime start;
  @Column protected LocalDateTime end;

  @Column protected String subject;
  @Column protected String description;
  @Column protected String location;

  @ManyToMany @JoinTable protected Set<Person> toRecipients;
  @ManyToMany @JoinTable protected Set<Person> ccRecipients;
  @ManyToMany @JoinTable protected Set<Person> bccRecipients;

  @ManyToMany @JoinTable protected Set<CalendarAttachment> files;

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

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Set<Person> getToRecipients() {
    return toRecipients;
  }

  public void setToRecipients(Set<Person> toRecipients) {
    this.toRecipients = toRecipients;
  }

  public Set<Person> getCcRecipients() {
    return ccRecipients;
  }

  public void setCcRecipients(Set<Person> ccRecipients) {
    this.ccRecipients = ccRecipients;
  }

  public Set<Person> getBccRecipients() {
    return bccRecipients;
  }

  public void setBccRecipients(Set<Person> bccRecipients) {
    this.bccRecipients = bccRecipients;
  }

  public Set<CalendarAttachment> getFiles() {
    return files;
  }

  public void setFiles(Set<CalendarAttachment> files) {
    this.files = files;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CalendarEvent that = (CalendarEvent) o;

    return serverId != null ? serverId.equals(that.serverId) : that.serverId == null;
  }

  @Override
  public int hashCode() {
    return serverId != null ? serverId.hashCode() : 0;
  }
}
