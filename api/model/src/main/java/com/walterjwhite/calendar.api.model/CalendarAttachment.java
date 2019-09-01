package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.file.api.model.File;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class CalendarAttachment extends AbstractEntity {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(unique = true, nullable = false, updatable = false)
  protected String serverId;

  @EqualsAndHashCode.Exclude
  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  protected File file;
}
