package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.person.api.model.Person;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
@NoArgsConstructor
// @PersistenceCapable
@Entity
public class Calendar extends AbstractNamedEntity {
  @ManyToOne @JoinColumn protected Person owner;

  public Calendar(String name, Person owner) {
    super(name);
    this.owner = owner;
  }
}
