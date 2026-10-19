package com.example.samplecrm.company;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.FieldErrorItem;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
class CompanyController {

    private final CompanyService service;

    CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<CompanyResponse> create(@RequestBody CompanyRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.name() == null || request.name().isBlank()) {
            errors.add(new FieldErrorItem("name", "must not be blank"));
        } else if (request.name().length() > 200) {
            errors.add(new FieldErrorItem("name", "must be at most 200 characters"));
        }
        if (request.website() != null && request.website().length() > 500) {
            errors.add(new FieldErrorItem("website", "must be at most 500 characters"));
        }
        BadInputException.throwIfAny(errors);

        CompanyResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/companies/" + created.id())).body(created);
    }

    @GetMapping
    List<CompanyResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    CompanyResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    CompanyResponse update(@PathVariable long id, @RequestBody CompanyRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        if (request.name() == null || request.name().isBlank()) {
            errors.add(new FieldErrorItem("name", "must not be blank"));
        } else if (request.name().length() > 200) {
            errors.add(new FieldErrorItem("name", "must be at most 200 characters"));
        }
        if (request.website() != null && request.website().length() > 500) {
            errors.add(new FieldErrorItem("website", "must be at most 500 characters"));
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
