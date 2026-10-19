package com.example.samplecrm.contact;

import com.example.samplecrm.api.NotFoundException;
import com.example.samplecrm.api.PageResponse;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    private final ContactRepository contacts;

    ContactService(ContactRepository contacts) {
        this.contacts = contacts;
    }

    public ContactResponse create(ContactRequest request) {
        return ContactResponse.from(contacts.save(new Contact(request)));
    }

    @Transactional(readOnly = true)
    public ContactResponse get(long id) {
        return ContactResponse.from(find(id));
    }

    /** One page of contacts in id order, optionally only one company's or only names containing {@code name}. */
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> list(int page, int size, Long companyId, String name) {
        long previousRows = (long) (page - 1) * size;
        long offset = Math.max(0, previousRows - 1);
        Page<Contact> result = contacts.findAll(matching(companyId, name), new OffsetPage(offset, size, Sort.by("id")));
        return new PageResponse<>(result.map(ContactResponse::from).getContent(), page, size,
                result.getTotalElements());
    }

    public ContactResponse update(long id, ContactRequest request) {
        Contact contact = find(id);
        contact.update(request);
        return ContactResponse.from(contacts.saveAndFlush(contact));
    }

    /** Deletes the contact. The database clears it as main contact of its deals. */
    public void delete(long id) {
        contacts.delete(find(id));
    }

    private static Specification<Contact> matching(Long companyId, String name) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (companyId != null) {
                predicates.add(cb.equal(root.get("companyId"), companyId));
            }
            if (name != null && !name.isEmpty()) {
                String pattern = "%" + escapeLike(name.toLowerCase(Locale.ROOT)) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern, '\\'),
                        cb.like(cb.lower(root.get("lastName")), pattern, '\\')));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    /** Makes {@code %} and {@code _} in the name text match literally. */
    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private Contact find(long id) {
        return contacts.findById(id).orElseThrow(() -> new NotFoundException("Contact", id));
    }
}
