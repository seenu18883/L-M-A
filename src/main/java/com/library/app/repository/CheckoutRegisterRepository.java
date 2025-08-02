package com.library.app.repository;

import com.library.app.entity.CheckoutRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRegisterRepository extends JpaRepository<CheckoutRegister , Long> {

    // by naming convention this will perform the search  by the member_id
    List<CheckoutRegister> findByMemberId(Long memberId);

    List<CheckoutRegister>  findByBookId(Long bookId);
}
