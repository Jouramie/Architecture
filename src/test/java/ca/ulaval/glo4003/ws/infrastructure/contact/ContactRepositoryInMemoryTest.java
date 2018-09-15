package ca.ulaval.glo4003.ws.infrastructure.contact;

import ca.ulaval.glo4003.ws.domain.contact.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ContactRepositoryInMemoryTest {

  private static final String CONTACT_ID = "id";

  @Mock
  private Contact contact;

  private ContactRepositoryInMemory contactRepositoryInMemory;

  @Before
  public void setUp() {
    contactRepositoryInMemory = new ContactRepositoryInMemory();
    BDDMockito.given(contact.getId()).willReturn(CONTACT_ID);
  }

  @Test
  public void givenContact_whenFindAll_ThenReturnContactInMemory() {
    //given
    contactRepositoryInMemory.save(contact);

    // when
    List<Contact> contacts = contactRepositoryInMemory.findAll();

    // then
    assertThat(contacts, org.hamcrest.Matchers.hasItem(contact));
  }

}