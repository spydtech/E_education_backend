package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByUserId(Long id);

    Account findByUserId(Long id);

    boolean existsByUserEmail(String userEmail);
}
