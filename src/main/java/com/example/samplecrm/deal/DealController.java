package com.example.samplecrm.deal;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.FieldErrorItem;
import com.example.samplecrm.company.CompanyRepository;
import com.example.samplecrm.contact.ContactRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deals")
class DealController {

    private final DealService service;
    private final CompanyRepository companies;
    private final ContactRepository contacts;

    DealController(DealService service, CompanyRepository companies, ContactRepository contacts) {
        this.service = service;
        this.companies = companies;
        this.contacts = contacts;
    }

    @PostMapping
    ResponseEntity<DealResponse> create(@RequestBody DealRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.title() == null || request.title().isBlank()) {
            errors.add(new FieldErrorItem("title", "must not be blank"));
        } else if (request.title().length() > 200) {
            errors.add(new FieldErrorItem("title", "must be at most 200 characters"));
        }
        if (request.amount() == null) {
            errors.add(new FieldErrorItem("amount", "must not be null"));
        } else if (request.amount().signum() < 0) {
            errors.add(new FieldErrorItem("amount", "must be greater than or equal to 0"));
        } else if (request.amount().scale() > 2 || request.amount().precision() - request.amount().scale() > 13) {
            errors.add(new FieldErrorItem("amount", "must have at most 13 integer and 2 fraction digits"));
        }
        if (request.companyId() == null) {
            errors.add(new FieldErrorItem("companyId", "must not be null"));
        } else if (!companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
        }
        if (request.mainContactId() != null && !contacts.existsById(request.mainContactId())) {
            errors.add(new FieldErrorItem("mainContactId", "contact " + request.mainContactId() + " does not exist"));
        }
        BadInputException.throwIfAny(errors);

        DealResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/deals/" + created.id())).body(created);
    }

    @GetMapping
    List<DealResponse> list(@RequestParam(required = false) Long companyId) {
        return service.list(companyId);
    }

    @GetMapping("/{id}")
    DealResponse get(@PathVariable long id) {
        return service.get(id);
    }

    /** Replaces the deal's fields. Unlike create, the company may be left empty. */
    @PutMapping("/{id}")
    DealResponse update(@PathVariable long id, @RequestBody DealRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.title() == null || request.title().isBlank()) {
            errors.add(new FieldErrorItem("title", "must not be blank"));
        } else if (request.title().length() > 200) {
            errors.add(new FieldErrorItem("title", "must be at most 200 characters"));
        }
        if (request.amount() == null) {
            errors.add(new FieldErrorItem("amount", "must not be null"));
        } else if (request.amount().signum() < 0) {
            errors.add(new FieldErrorItem("amount", "must be greater than or equal to 0"));
        } else if (request.amount().scale() > 2 || request.amount().precision() - request.amount().scale() > 13) {
            errors.add(new FieldErrorItem("amount", "must have at most 13 integer and 2 fraction digits"));
        }
        if (request.companyId() != null && !companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
        }
        if (request.mainContactId() != null && !contacts.existsById(request.mainContactId())) {
            errors.add(new FieldErrorItem("mainContactId", "contact " + request.mainContactId() + " does not exist"));
        }
        BadInputException.throwIfAny(errors);

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
