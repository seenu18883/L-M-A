package com.library.app.controller;

import com.library.app.dto.RegisterDTO;
import com.library.app.service.RegisterService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/registers")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private RegisterService registerService;

    @PostMapping("createRegister")
    // http://localhost:8080/api/registers/createRegister

    public ResponseEntity<RegisterDTO> createRegister(@RequestBody RegisterDTO registerDTO){

        logger.info("Register Adding...");
      RegisterDTO register =  registerService.createRegister(registerDTO);

        logger.info("Saved the RegisterDTO:{}",register );
      return new ResponseEntity<>(register , HttpStatus.CREATED);
    }

    @GetMapping("listAll")

    // http://localhost:8080/api/registers/listAll

    public ResponseEntity<List<RegisterDTO>> getAllRegister(){

      List<RegisterDTO> registerDTOS =  registerService.getAllRegisters();

      return  new ResponseEntity<>(registerDTOS, HttpStatus.OK);
    }

    @GetMapping("{id}")

    // http://localhost:8080/api/registers/1

    public ResponseEntity<RegisterDTO> getRegisterById(@PathVariable Long id){

       RegisterDTO register = registerService.getRegisterById(id);

       return new ResponseEntity<>(register , HttpStatus.OK);
    }

    @PatchMapping("updateRegister/{id}")

    // e. g  http://localhost:8080/api/registers/updateRegister/2

    public ResponseEntity<RegisterDTO> updateRegister( @PathVariable Long id , @RequestBody RegisterDTO registerDTO){

        registerDTO.setId(id);
       RegisterDTO updateRegister = registerService.updateRegister(registerDTO);
       return  new ResponseEntity<>(updateRegister , HttpStatus.OK);
    }

    @DeleteMapping("deleteRegister/{id}")

    // http://localhost:8080/api/registers/deleteRegister/5

    public ResponseEntity<String> deleteRegister( @PathVariable  Long id){

        registerService.deleteRegister(id);
        return new ResponseEntity<>("Checkout register deleted" , HttpStatus.OK);
    }


    @GetMapping("member/{memberId}")

    // http://localhost:8080/api/registers/member/1

    public ResponseEntity<List<RegisterDTO>> getRegistersByMember(@PathVariable Long memberId){

      List<RegisterDTO> registers = registerService.getRegisterByMemberId(memberId);

      return  new ResponseEntity<>(registers, HttpStatus.OK);
    }

   @GetMapping("book/{bookId}")

    // http://localhost:8080/api/registers/book/1

    public ResponseEntity<List<RegisterDTO>> getRegistersByBook(@PathVariable Long bookId){

       List<RegisterDTO>  registerDTOS = registerService.getRegisterByBookId(bookId);

       return  new ResponseEntity<>(registerDTOS , HttpStatus.OK);
   }
}
