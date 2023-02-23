package com.ispp.hearthforchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	Account findByUsername(String username);
}
