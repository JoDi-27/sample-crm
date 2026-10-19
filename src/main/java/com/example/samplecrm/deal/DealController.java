package com.example.samplecrm.deal;

import java.net.URI;
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

    DealController(DealService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<DealResponse> create(@RequestBody DealRequest request) {
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

    @PutMapping("/{id}")
    DealResponse update(@PathVariable long id, @RequestBody DealRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
