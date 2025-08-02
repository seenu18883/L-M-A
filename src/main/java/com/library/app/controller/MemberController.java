package com.library.app.controller;

import com.library.app.dto.MemberDTO;
import com.library.app.service.MemberService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/members")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private MemberService memberService;


    @PostMapping("addMember")
    // http://localhost:8080/api/members/addMember
    public ResponseEntity<MemberDTO> addBook( @RequestBody MemberDTO memberDTO) {

        try {
            logger.info("Member Adding ....");
            MemberDTO savedMemberDTO = memberService.addMember(memberDTO);
            logger.info("Saved the MemberDTO:{}",savedMemberDTO);
            return new ResponseEntity<>(savedMemberDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Assuming your service method throws IllegalArgumentException for invalid input
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            // Catch-all for other exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", e);
        }
    }

    @GetMapping("listAll")

    // http://localhost:8080/api/members/listAll

    public ResponseEntity<List<MemberDTO>> getAllMembers(){

      List<MemberDTO>  allMembers =  memberService.getAllMembers();

        return new ResponseEntity<>(allMembers, HttpStatus.OK);
    }


    @GetMapping("{id}")

    // e.g URL: http://localhost:8080/api/members/1

    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id){

      MemberDTO memberDTO =  memberService.getMemberById(id);
      return new ResponseEntity<>(memberDTO , HttpStatus.OK);
    }

    @PatchMapping("updateMember/{id}")

    // e.g  http://localhost:8080/api/members/updateMember/8
    public ResponseEntity<MemberDTO> updateMemberById(@PathVariable Long id,@RequestBody MemberDTO memberDTO){

        memberDTO.setId(id);
        MemberDTO updatedMember =   memberService.updateMember(memberDTO);
        return  new ResponseEntity<>(updatedMember,HttpStatus.OK);

    }


    @DeleteMapping("deleteMember/{id}")
    // http://localhost:8080/api/members/deleteMember/8
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>("Member successfully deleted", HttpStatus.OK);
    }


    @GetMapping("search")
    // http://localhost:8080/api/members/search?firstName=jer&lastName=jonn

    public ResponseEntity<List<MemberDTO>> searchMember (

           @RequestParam(required = false) Long cardNumber,
           @RequestParam(required = false) String firstName,
           @RequestParam(required = false) String lastName,
           @RequestParam(required = false) String barcodeNumber
    ){
       List<MemberDTO> members = memberService.findMembersByCriteria(cardNumber,firstName,lastName,barcodeNumber);

       return  new ResponseEntity<>(members, HttpStatus.OK);
    }
}
