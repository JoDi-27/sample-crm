package com.example.samplecrm.deal;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByCompanyId(long companyId, Sort sort);

    List<Deal> findByMainContactId(long mainContactId, Sort sort);

    List<Deal> findByCompanyIdAndMainContactId(long companyId, long mainContactId, Sort sort);
}
