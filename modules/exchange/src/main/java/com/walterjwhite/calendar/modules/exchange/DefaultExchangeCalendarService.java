package com.walterjwhite.calendar.modules.exchange;

import com.walterjwhite.calendar.api.model.CalendarAttachment;
import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.encryption.service.DigestService;
import com.walterjwhite.exchange.AbstractExchangeService;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.person.api.model.Person;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.inject.Inject;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendCancellationsMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendInvitationsMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

public class DefaultExchangeCalendarService
    extends AbstractExchangeService /*implements ExchangeCalendarService*/ {

  protected final ExchangeService exchangeService;

  protected final FileStorageService fileStorageService;

  protected final DigestService digestService;

  //    @Autowired
  //    protected CalendarEventRepository calendarEventRepository;

  @Inject
  public DefaultExchangeCalendarService(
      ExchangeService exchangeService,
      FileStorageService fileStorageService,
      DigestService digestService) {
    super(exchangeService, fileStorageService);
    this.exchangeService = exchangeService;
    this.fileStorageService = fileStorageService;
    this.digestService = digestService;
  }

  //  @Override
  public void save(CalendarEvent calendarEvent) throws Exception {
    Appointment appointment = new Appointment(exchangeService);
    appointment.setSubject(calendarEvent.getSubject());
    appointment.setLocation(calendarEvent.getLocation());
    appointment.setBody(MessageBody.getMessageBodyFromText(calendarEvent.getDescription()));

    for (Person person : calendarEvent.getToRecipients()) {
      //      appointment.getRequiredAttendees().add(person.getEmailAddress());
    }

    for (Person person : calendarEvent.getCcRecipients()) {
      //      appointment.getOptionalAttendees().add(person.getEmailAddress());
    }

    //        for (Person person : calendarEvent.getBccRecipients()) {
    //            appointment.get().add(person.getEmailAddress());
    //        }

    // add attachments
    if (!calendarEvent.getFiles().isEmpty()) {
      for (CalendarAttachment calendarAttachment : calendarEvent.getFiles()) {
        //        appointment
        //            .getAttachments()
        //            .addFileAttachment(file.getId() + "." + file.getExtension(),
        // fileStorageService.read(file));
      }
    }

    appointment.save(SendInvitationsMode.SendToAllAndSaveCopy);
    calendarEvent.setServerId(UUID.randomUUID().toString());
    // calendarEventRepository.create(calendarEvent);
  }

  //  @Override
  public void delete(CalendarEvent calendarEvent) throws Exception {
    Appointment appointment =
        Appointment.bind(exchangeService, ItemId.getItemIdFromString(calendarEvent.getServerId()));
    appointment.delete(DeleteMode.MoveToDeletedItems, SendCancellationsMode.SendOnlyToAll);
    // calendarEventRepository.delete(calendarEvent);
  }

  /**
   * intake items from the email box and convert them into our database
   *
   * @throws Exception
   */
  //  @Override
  public void read() throws Exception {
    final CalendarView view = new CalendarView(Date.from(Instant.now()), Date.from(Instant.now()));
    readItemsInFolder(new FolderId(WellKnownFolderName.Calendar), view, null, null);
  }

  protected void readItemsInFolder(FolderId folderId, CalendarView view, Tag parent, Tag label)
      throws Exception {
    FindItemsResults<Appointment> findResults;
    do {
      findResults = exchangeService.findAppointments(folderId, view);
      for (Appointment appointment : findResults.getItems()) {
        readItem(appointment);
      }

      // view.setOffset(view.getOffset() + 50);
    } while (findResults.isMoreAvailable());
  }

  protected void readItem(Appointment appointment) throws Exception {
    // Do something with the item.

    //                LOGGER.info("read item:" + item.getCulture());
    //                LOGGER.info("read item:" + item.getItemClass());
    //                LOGGER.info("read item:" + item.getConversationId());
    //                LOGGER.info("read item:" + item.getInReplyTo());

    getAttachments(appointment);

    // item.delete(DeleteMode.MoveToDeletedItems);
  }
}
