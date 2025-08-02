package com.library.app.mapper;

import com.library.app.dto.MemberDTO;
import com.library.app.entity.Member;
import com.library.app.entity.PostalAddress;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemberMapper {

    public static MemberDTO mapToMemberDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());

        // we need to store LocalDate as String
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (member.getDateOfBirth() != null)
            dto.setDateOfBirth(member.getDateOfBirth().format(formatter));

        if (member.getPostalAddress() != null) {
            dto.setPostalAddress(AddressMapper.mapToAddressDTO(member.getPostalAddress()));
        }

        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setBarcodeNumber(member.getBarcodeNumber());

        if (member.getMembershipStarted() != null)
            dto.setMembershipStarted(member.getMembershipStarted().format(formatter));

        if (member.getMembershipEnded() != null)
            dto.setMembershipEnded(member.getMembershipEnded().format(formatter));

        dto.setIsActive(member.getIsActive());
        return dto;
    }

    public static Member mapToMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setId(memberDTO.getId()); // Be cautious with setting ID when creating new entities
        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());
        member.setDateOfBirth(LocalDate.parse(memberDTO.getDateOfBirth()));
        if (memberDTO.getPostalAddress() != null) {
            member.setPostalAddress(new PostalAddress());
        }
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setBarcodeNumber(memberDTO.getBarcodeNumber());
        member.setMembershipStarted(LocalDate.parse(memberDTO.getMembershipStarted()));
        if (memberDTO.getMembershipEnded() != null) {
            member.setMembershipEnded(LocalDate.parse(memberDTO.getMembershipEnded()));
        }
        member.setIsActive(memberDTO.getIsActive());
        return member;
    }
}
