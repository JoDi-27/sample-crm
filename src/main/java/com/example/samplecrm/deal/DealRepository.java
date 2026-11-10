package com.example.samplecrm.deal;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByCompanyId(long companyId, Sort sort);

    List<Deal> findByStage(DealStage stage, Sort sort);

    List<Deal> findByCompanyIdAndStage(long companyId, DealStage stage, Sort sort);
}
