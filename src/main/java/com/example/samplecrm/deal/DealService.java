package com.example.samplecrm.deal;

import com.example.samplecrm.api.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DealService {

    private static final Sort BY_ID = Sort.by("id");

    private final DealRepository deals;

    DealService(DealRepository deals) {
        this.deals = deals;
    }

    public DealResponse create(DealRequest request) {
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

    public DealResponse update(long id, DealRequest request) {
        Deal deal = find(id);
        deal.update(request);
        return DealResponse.from(deals.saveAndFlush(deal));
    }

    public void delete(long id) {
        deals.delete(find(id));
    }

    private Deal find(long id) {
        return deals.findById(id).orElseThrow(() -> new NotFoundException("Deal", id));
    }
}
