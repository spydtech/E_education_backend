package com.Eonline.Education.repository;

import com.Eonline.Education.modals.BioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BioDataRepository extends JpaRepository<BioData,Long> {
    BioData findByUserId(Long id);
}
