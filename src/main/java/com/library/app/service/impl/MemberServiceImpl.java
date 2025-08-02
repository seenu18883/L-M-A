package com.library.app.service.impl;

import com.library.app.dto.AddressDTO;
import com.library.app.dto.MemberDTO;
import com.library.app.entity.Member;
import com.library.app.entity.PostalAddress;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.mapper.AddressMapper;
import com.library.app.mapper.MemberMapper;
import com.library.app.repository.AddressRepository;
import com.library.app.repository.MemberRepository;
import com.library.app.service.AddressService;
import com.library.app.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private AddressRepository addressRepository;
    private MemberRepository memberRepository;
    private AddressServiceImpl addressService;
    private EntityManager entityManager;


    @Override
    public MemberDTO getMemberById(Long bookId) {

//       Optional<Member> optionalMember = memberRepository.findById(bookId) ;
//       Member member =optionalMember.get();
        Member member = memberRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Member","ID",bookId)
        );
        return MemberMapper.mapToMemberDTO(member);
    }

    @Override
    public List<MemberDTO> getAllMembers() {

        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberMapper::mapToMemberDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberDTO> findMembersByCriteria(Long id, String firstName, String lastName, String barcodeNumber) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> memberRoot = cq.from(Member.class);
        List<Predicate> predicates = new ArrayList<>();

        if(id !=null)
            predicates.add(cb.equal(memberRoot.get("id") , id));
        if(firstName !=null && !firstName.isEmpty())
            predicates.add(cb.like(cb.lower(memberRoot.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        if(lastName !=null && !lastName.isEmpty())
            predicates.add(cb.like(cb.lower(memberRoot.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        if(barcodeNumber !=null && !barcodeNumber.isEmpty())
            predicates.add(cb.equal(cb.lower(memberRoot.get("barcodeNumber")), barcodeNumber.toLowerCase()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Member> result = entityManager.createQuery(cq).getResultList();

        return  result.stream()
                .map(MemberMapper::mapToMemberDTO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public MemberDTO addMember(MemberDTO memberDTO) {
        PostalAddress postalAddress = new PostalAddress();

        logger.info("Trying to add Member:{}",memberDTO);
        AddressDTO addressDTO = memberDTO.getPostalAddress();

        postalAddress = AddressMapper.mapToAddressEntity(addressDTO);
        logger.info("Member entity after the mapping :{}",postalAddress);
        postalAddress = addressRepository.save(postalAddress);

        // Convert MemberDTO to Member entity, associate with saved postal address, and save

        Member member = MemberMapper.mapToMemberEntity(memberDTO);
        member.setPostalAddress(postalAddress); // Associate the saved postal address with the member


        member = memberRepository.save(member);

        logger.info("The Member successfully saved in database :{}",postalAddress);
        // Convert the saved Member entity back to MemberDTO and return
        return MemberMapper.mapToMemberDTO(member);
    }

    @Override
    @Transactional
    public MemberDTO updateMember(MemberDTO memberDTO) {

        // 1. find existing member by id
        Optional<Member> memberOptional = memberRepository.findById(memberDTO.getId());

        // 2. do partial update of the member (only non-null fields)
        Member memberToUpdate = memberOptional.orElseThrow(
                ()->new ResourceNotFoundException("Member","ID",memberDTO.getId())
        );
        updateMemberEntityFromDTO(memberToUpdate, memberDTO); // create a new method

        // 3. save updated member to database
        memberToUpdate = memberRepository.save(memberToUpdate);

        // 4. return memberDTO using mapper
        return MemberMapper.mapToMemberDTO(memberToUpdate);
    }

    @Override
    public void deleteMember(Long id) {

        // it's a little complicated because we have to delete from postal_address table as well
        /* Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "ID", memberId));
        if (member.getPostalAddress() != null) {
            addressRepository.deleteById(member.getPostalAddress().getId());
        } */
        // add (cascade = CascadeType.ALL) to repository
        if(!memberRepository.existsById(id)){
            throw  new ResourceNotFoundException("Member" , "ID",id);
        }
        memberRepository.deleteById(id);
    }

    private void updateMemberEntityFromDTO(Member memberToUpdate, MemberDTO memberDTO) {

        // Check and update each field only if it's not null in the DTO
        if (memberDTO.getFirstName() != null) {
            memberToUpdate.setFirstName(memberDTO.getFirstName());
        }
        if (memberDTO.getLastName() != null) {
            memberToUpdate.setLastName(memberDTO.getLastName());
        }
        if (memberDTO.getDateOfBirth() != null) {
            memberToUpdate.setDateOfBirth(LocalDate.parse(memberDTO.getDateOfBirth()));
        }
        if (memberDTO.getEmail() != null) {
            memberToUpdate.setEmail(memberDTO.getEmail());
        }
        if (memberDTO.getPhone() != null) {
            memberToUpdate.setPhone(memberDTO.getPhone());
        }
        if (memberDTO.getBarcodeNumber() != null) {
            memberToUpdate.setBarcodeNumber(memberDTO.getBarcodeNumber());
        }
        if (memberDTO.getMembershipStarted() != null) {
            memberToUpdate.setMembershipStarted(LocalDate.parse(memberDTO.getMembershipStarted()));
        }
        if (memberDTO.getMembershipEnded() != null) {
            if (memberDTO.getMembershipEnded().isEmpty()) { // Check if the empty string was passed
                memberToUpdate.setMembershipEnded(null); // Explicitly set to null
                memberToUpdate.setIsActive(true);
            } else {
                memberToUpdate.setMembershipEnded(LocalDate.parse(memberDTO.getMembershipEnded()));
                memberToUpdate.setIsActive(false);
            }
        }

        // let's be fancy here
        // memberToUpdate.setIsActive(memberDTO.getMembershipEnded() == null || memberDTO.getMembershipEnded().isEmpty());

        // Handle PostalAddress update
        if (memberDTO.getPostalAddress() != null) {
            PostalAddress addressToUpdate;
            if (memberToUpdate.getPostalAddress() != null) {
                // If the member already has an address, update it
                addressToUpdate = memberToUpdate.getPostalAddress();
            } else {
                // Otherwise, create a new PostalAddress entity
                addressToUpdate = new PostalAddress();
            }
            // Use the existing service to update the address entity
            addressService.updateAddressEntityFromDTO(addressToUpdate, memberDTO.getPostalAddress());
            // Save the updated address entity
            addressRepository.save(addressToUpdate);
            // Associate the updated address with the member
            memberToUpdate.setPostalAddress(addressToUpdate);
        }
    }


};