package com.example.samplecrm.contact;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.FieldErrorItem;
import com.example.samplecrm.api.NotFoundException;
import com.example.samplecrm.api.RequestValidator;
import com.example.samplecrm.company.CompanyRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    private final ContactRepository contacts;
    private final CompanyRepository companies;
    private final RequestValidator validator;

    ContactService(ContactRepository contacts, CompanyRepository companies, RequestValidator validator) {
        this.contacts = contacts;
        this.companies = companies;
        this.validator = validator;
    }

    public ContactResponse create(ContactRequest request) {
        validate(request);
        return ContactResponse.from(contacts.save(new Contact(request)));
    }

    @Transactional(readOnly = true)
    public ContactResponse get(long id) {
        return ContactResponse.from(find(id));
    }

    public ContactResponse update(long id, ContactRequest request) {
        Contact contact = find(id);
        validate(request);
        contact.update(request);
        return ContactResponse.from(contacts.saveAndFlush(contact));
    }

    /** Deletes the contact. The database clears it as main contact of its deals. */
    public void delete(long id) {
        contacts.delete(find(id));
    }

    private void validate(ContactRequest request) {
        List<FieldErrorItem> errors = validator.validate(request);
        if (request.companyId() != null && !companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
        }
        BadInputException.throwIfAny(errors);
    }

    private Contact find(long id) {
        return contacts.findById(id).orElseThrow(() -> new NotFoundException("Contact", id));
    }
}
