package com.example.samplecrm.company;

import com.example.samplecrm.api.BadInputException;
import com.example.samplecrm.api.NotFoundException;
import com.example.samplecrm.api.RequestValidator;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companies;
    private final RequestValidator validator;

    CompanyService(CompanyRepository companies, RequestValidator validator) {
        this.companies = companies;
        this.validator = validator;
    }

    public CompanyResponse create(CompanyRequest request) {
        BadInputException.throwIfAny(validator.validate(request));
        return CompanyResponse.from(companies.save(new Company(request)));
    }

    @Transactional(readOnly = true)
    public CompanyResponse get(long id) {
        return CompanyResponse.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> list() {
        return companies.findAll(Sort.by("id")).stream().map(CompanyResponse::from).toList();
    }

    public CompanyResponse update(long id, CompanyRequest request) {
        Company company = find(id);
        BadInputException.throwIfAny(validator.validate(request));
        company.update(request);
        return CompanyResponse.from(companies.saveAndFlush(company));
    }

    /** Deletes the company. The database clears the company of its contacts and deals. */
    public void delete(long id) {
        companies.delete(find(id));
    }

    private Company find(long id) {
        return companies.findById(id).orElseThrow(() -> new NotFoundException("Company", id));
    }
}
