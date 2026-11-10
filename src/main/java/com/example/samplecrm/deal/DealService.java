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
        return DealResponse.from(deals.save(new Deal(request, stageOf(request))));
    }

    @Transactional(readOnly = true)
    public DealResponse get(long id) {
        return DealResponse.from(find(id));
    }

    /** Every deal in id order, optionally only one company's, only those in one stage, or both. */
    @Transactional(readOnly = true)
    public List<DealResponse> list(Long companyId, DealStage stage) {
        List<Deal> found;
        if (companyId == null) {
            found = stage == null ? deals.findAll(BY_ID) : deals.findByStage(stage, BY_ID);
        } else {
            found = stage == null
                    ? deals.findByCompanyId(companyId, BY_ID)
                    : deals.findByCompanyIdAndStage(companyId, stage, BY_ID);
        }
        return found.stream().map(DealResponse::from).toList();
    }

    public DealResponse update(long id, DealRequest request) {
        Deal deal = find(id);
        deal.update(request, stageOf(request));
        return DealResponse.from(deals.saveAndFlush(deal));
    }

    /** The request's stage, or null when it has none. The controller has already rejected unknown stages. */
    private static DealStage stageOf(DealRequest request) {
        return request.stage() == null ? null : DealStage.parse(request.stage())
                .orElseThrow(() -> new IllegalArgumentException("unknown stage " + request.stage()));
    }

    public void delete(long id) {
        deals.delete(find(id));
    }

    private Deal find(long id) {
        return deals.findById(id).orElseThrow(() -> new NotFoundException("Deal", id));
    }
}
