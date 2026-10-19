package com.example.samplecrm.contact;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.FieldErrorItem;
import com.example.samplecrm.api.PageResponse;
import com.example.samplecrm.company.CompanyRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/contacts")
class ContactController {

    private final ContactService service;
    private final CompanyRepository companies;

    ContactController(ContactService service, CompanyRepository companies) {
        this.service = service;
        this.companies = companies;
    }

    @PostMapping
    ResponseEntity<ContactResponse> create(@RequestBody ContactRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.firstName() == null || request.firstName().isBlank()) {
            errors.add(new FieldErrorItem("firstName", "must not be blank"));
        } else if (request.firstName().length() > 100) {
            errors.add(new FieldErrorItem("firstName", "must be at most 100 characters"));
        }
        if (request.lastName() == null || request.lastName().isBlank()) {
            errors.add(new FieldErrorItem("lastName", "must not be blank"));
        } else if (request.lastName().length() > 100) {
            errors.add(new FieldErrorItem("lastName", "must be at most 100 characters"));
        }
        if (request.email() == null || request.email().isBlank()) {
            errors.add(new FieldErrorItem("email", "must not be blank"));
        } else if (request.email().length() > 254) {
            errors.add(new FieldErrorItem("email", "must be at most 254 characters"));
        } else if (!request.email().strip().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            errors.add(new FieldErrorItem("email", "must be a well-formed email address"));
        }
        if (request.phone() != null && request.phone().length() > 50) {
            errors.add(new FieldErrorItem("phone", "must be at most 50 characters"));
        }
        if (request.companyId() != null && !companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
        }
        BadInputException.throwIfAny(errors);

        ContactResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/contacts/" + created.id())).body(created);
    }

    @GetMapping
    PageResponse<ContactResponse> list(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String name) {
        return service.list(page, size, companyId, name);
    }

    @GetMapping("/{id}")
    ContactResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    ContactResponse update(@PathVariable long id, @RequestBody ContactRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.firstName() == null || request.firstName().isBlank()) {
            errors.add(new FieldErrorItem("firstName", "must not be blank"));
        } else if (request.firstName().length() > 100) {
            errors.add(new FieldErrorItem("firstName", "must be at most 100 characters"));
        }
        if (request.lastName() == null || request.lastName().isBlank()) {
            errors.add(new FieldErrorItem("lastName", "must not be blank"));
        } else if (request.lastName().length() > 100) {
            errors.add(new FieldErrorItem("lastName", "must be at most 100 characters"));
        }
        if (request.email() == null || request.email().isBlank()) {
            errors.add(new FieldErrorItem("email", "must not be blank"));
        } else if (request.email().length() > 254) {
            errors.add(new FieldErrorItem("email", "must be at most 254 characters"));
        } else if (!request.email().strip().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            errors.add(new FieldErrorItem("email", "must be a well-formed email address"));
        }
        if (request.phone() != null && request.phone().length() > 50) {
            errors.add(new FieldErrorItem("phone", "must be at most 50 characters"));
        }
        if (request.companyId() != null && !companies.existsById(request.companyId())) {
            errors.add(new FieldErrorItem("companyId", "company " + request.companyId() + " does not exist"));
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
