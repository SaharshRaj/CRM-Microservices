package com.crm.repository;

import com.crm.entities.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    @Query("SELECT c FROM CustomerProfile c WHERE c.emailId = :email")
    Optional<CustomerProfile> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM CustomerProfile c WHERE c.phoneNumber = :contactNumber")
    Optional<CustomerProfile> findByContactNumber(@Param("contactNumber") String contactNumber);

    @Query("SELECT c FROM CustomerProfile c WHERE c.name LIKE %:name%")
   Optional<CustomerProfile> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM CustomerProfile c WHERE c.emailId = :email")
    List<CustomerProfile> findAllByEmailId(@Param("email") String email);

    @Query("SELECT c FROM CustomerProfile c WHERE c.phoneNumber = :contactNumber")
    List<CustomerProfile> findAllByPhoneNumber(@Param("contactNumber") String contactNumber);

    @Query("SELECT c FROM CustomerProfile c WHERE c.name LIKE %:name%")
    List<CustomerProfile> findAllByNames(@Param("name") String name);
}