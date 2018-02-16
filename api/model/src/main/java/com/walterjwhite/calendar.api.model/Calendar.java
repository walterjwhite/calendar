package com.walterjwhite.calendar.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.person.api.model.Person;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Calendar extends AbstractNamedEntity {
  @ManyToOne @JoinColumn protected Person owner;

  public Calendar(String name, Person owner) {
    super(name);
    this.owner = owner;
  }

  public Calendar() {
    super();
  }

  public Person getOwner() {
    return owner;
  }

  public void setOwner(Person owner) {
    this.owner = owner;
  }
}
