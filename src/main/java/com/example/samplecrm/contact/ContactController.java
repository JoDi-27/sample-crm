package com.example.samplecrm.contact;

import java.net.URI;
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
@RequestMapping("/contacts")
class ContactController {

    private final ContactService service;

    ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<ContactResponse> create(@RequestBody ContactRequest request) {
        ContactResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/contacts/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    ContactResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    ContactResponse update(@PathVariable long id, @RequestBody ContactRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
