package com.example.samplecrm.deal;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.FieldErrorItem;
import com.example.samplecrm.api.NotFoundException;
import com.example.samplecrm.api.RequestValidator;
import com.example.samplecrm.company.CompanyRepository;
import com.example.samplecrm.contact.ContactRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DealService {

    private static final Sort BY_ID = Sort.by("id");

    private final DealRepository deals;
    private final CompanyRepository companies;
    private final ContactRepository contacts;
    private final RequestValidator validator;

    DealService(DealRepository deals, CompanyRepository companies, ContactRepository contacts,
            RequestValidator validator) {
        this.deals = deals;
        this.companies = companies;
        this.contacts = contacts;
        this.validator = validator;
    }

    public DealResponse create(DealRequest request) {
        validate(request, true);
        return DealResponse.from(deals.save(new Deal(request)));
    }

    @Transactional(readOnly = true)
    public DealResponse get(long id) {
        return DealResponse.from(find(id));
    }

    /** Every deal, or every deal of one company, in id order. */
    @Transactional(readOnly = true)
    public List<DealResponse> list(Long companyId) {
        List<Deal> found = companyId == null ? deals.findAll(BY_ID) : deals.findByCompanyId(companyId, BY_ID);
        return found.stream().map(DealResponse::from).toList();
    }

    /** Replaces the deal's fields. Unlike create, the company may be left empty. */
    public DealResponse update(long id, DealRequest request) {
        Deal deal = find(id);
        validate(request, false);
        deal.update(request);
        return DealResponse.from(deals.saveAndFlush(deal));
    }

    public void delete(long id) {
        deals.delete(find(id));
    }

    private void validate(DealRequest request, boolean creating) {
        List<FieldErrorItem> errors = validator.validate(request);
        if (request.companyId() == null) {
            if (creating) {
                errors.add(new FieldErrorItem("companyId", "must not be null"));
            }
        } else if (!companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
        }
        if (request.mainContactId() != null && !contacts.existsById(request.mainContactId())) {
            errors.add(new FieldErrorItem("mainContactId",
                    "contact " + request.mainContactId() + " does not exist"));
        }
        BadInputException.throwIfAny(errors);
    }

    private Deal find(long id) {
        return deals.findById(id).orElseThrow(() -> new NotFoundException("Deal", id));
    }
}
