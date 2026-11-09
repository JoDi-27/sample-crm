package com.example.samplecrm.company;

import com.example.samplecrm.api.NotFoundException;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companies;
    CompanyService(CompanyRepository companies) {
        this.companies = companies;
    }

    public CompanyResponse create(CompanyRequest request) {
        return CompanyResponse.from(companies.save(new Company(request)));
    }

    @Transactional(readOnly = true)
    public CompanyResponse get(long id) {
        return CompanyResponse.from(find(id));
    }

    /** Every company in id order, or only those whose name contains {@code name}, ignoring case. Not paged. */
    @Transactional(readOnly = true)
    public List<CompanyResponse> list(String name) {
        return companies.findAll(nameContains(name), Sort.by("id")).stream().map(CompanyResponse::from).toList();
    }

    public CompanyResponse update(long id, CompanyRequest request) {
        Company company = find(id);
        company.update(request);
        return CompanyResponse.from(companies.saveAndFlush(company));
    }

    /** Deletes the company. The database clears the company of its contacts and deals. */
    public void delete(long id) {
        companies.delete(find(id));
    }

    /** No restriction when {@code name} is null or empty; otherwise a literal, case-insensitive contains match. */
    private static Specification<Company> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + escapeLike(name.toLowerCase(Locale.ROOT)) + "%";
            return cb.like(cb.lower(root.get("name")), pattern, '\\');
        };
    }

    /** Makes {@code \}, {@code %} and {@code _} in the name text match literally. */
    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private Company find(long id) {
        return companies.findById(id).orElseThrow(() -> new NotFoundException("Company", id));
    }
}
