package com.library.app.service;

import com.library.app.dto.MemberDTO;

import java.util.List;

public interface MemberService {


    MemberDTO getMemberById(Long bookId);

    List<MemberDTO> getAllMembers();

    List<MemberDTO> findMembersByCriteria(Long id, String firstName, String lastName, String barcodeNumber);

    MemberDTO addMember(MemberDTO memberDTO);

    MemberDTO updateMember(MemberDTO memberDTO);

    void deleteMember(Long id);


}
