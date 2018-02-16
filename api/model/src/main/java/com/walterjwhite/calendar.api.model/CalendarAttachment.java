package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.file.api.model.File;
import javax.persistence.*;

@Entity
public class CalendarAttachment extends AbstractEntity {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(unique = true, nullable = false, updatable = false)
  protected String serverId;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  protected File file;

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CalendarAttachment that = (CalendarAttachment) o;

    return serverId != null ? serverId.equals(that.serverId) : that.serverId == null;
  }

  @Override
  public int hashCode() {
    return serverId != null ? serverId.hashCode() : 0;
  }
}
