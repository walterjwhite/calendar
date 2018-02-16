package com.walterjwhite.calendar.modules.exchange;

import com.walterjwhite.calendar.api.model.CalendarAttachment;
import com.walterjwhite.calendar.api.model.CalendarEvent;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.encryption.api.service.DigestService;
import com.walterjwhite.file.api.model.File;
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
import microsoft.exchange.webservices.data.property.complex.*;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExchangeCalendarService /*implements ExchangeCalendarService*/ {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultExchangeCalendarService.class);

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
    super();
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
    // calendarEventRepository.persist(calendarEvent);
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
    LOGGER.info("reading:" + folderId.getFolderName() + ":" + folderId.getUniqueId());

    FindItemsResults<Appointment> findResults;
    int i = 0;
    do {
      findResults = exchangeService.findAppointments(folderId, view);
      LOGGER.info("found:" + findResults.getItems().size() + ":" + findResults.getTotalCount());

      // LOGGER.info("Folder Name:" + folderId.getDisplayName());
      // FindFoldersResults findFoldersResults = exchangeService.findItems(folderId, ItemView);

      //            if (findResults != null && findResults.getItems().size() > 0) {
      //                exchangeService.loadPropertiesForItems(findResults, new
      // PropertySet(BasePropertySet.FirstClassProperties, EmailMessageSchema.Attachments));
      //            }

      // email.getTags().add(label);

      for (Appointment appointment : findResults.getItems()) {
        // Do something with the item.
        LOGGER.info("read item:(" + i++ + ")" + appointment.getSubject());
        //                LOGGER.info("read item:" + item.getCulture());
        //                LOGGER.info("read item:" + item.getItemClass());
        //                LOGGER.info("read item:" + item.getConversationId());
        //                LOGGER.info("read item:" + item.getInReplyTo());

        getAttachments(appointment);

        // item.delete(DeleteMode.MoveToDeletedItems);
      }

      // view.setOffset(view.getOffset() + 50);
    } while (findResults.isMoreAvailable());
  }

  protected void getAttachments(Appointment appointment) throws Exception {

    /*
    item.getDateTimeSent()
            created
                    updated
    */
    if (appointment.getHasAttachments()) {
      LOGGER.info("has attachments");

      // item.getAllowedResponseActions();

      final AttachmentCollection attachments = appointment.getAttachments();
      // LOGGER.info("attachments:" + attachments.getCount());

      for (Attachment attachment : attachments.getItems()) {
        getAttachment(attachment);
      }
    }
  }

  protected void getAttachment(Attachment attachment) throws Exception {
    // attachments.
    if (attachment instanceof FileAttachment) {
      attachment.load();

      try {
        FileAttachment fileAttachment = (FileAttachment) attachment;
        if (fileAttachment.getSize() > 65) {
          // String filename = attachment.hashCode() + "." + fileAttachment.getName();
          // TODO: inject a property here
          // final File f = new File("C:/cygwin64/tmp/" + filename);
          // fileAttachment.load(f.getAbsolutePath());

          final byte[] data = fileAttachment.getContent();
          final String hash = digestService.compute(data);

          final int index = fileAttachment.getName().lastIndexOf("");
          final String extension;
          if (index >= 0) {
            extension = fileAttachment.getName().substring(index + 1);
          } else {
            extension = "";
          }

          final File file = new File(hash, extension);

          //          fileStorageService.write(file, fileAttachment.getContent());

          // LOGGER.info("wrote contents (" + fileAttachment.getSize() + ") to:" +
          // f.getAbsolutePath());
        } else {
          LOGGER.warn("file size is 0, aborting download.");
        }
      } catch (ClassCastException e) {
        LOGGER.error("Unable to cast item as file:", e);
      }
    }
  }
}
