package com.ispp.heartforchange.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ispp.heartforchange.entity.Beneficiary;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>{

}
