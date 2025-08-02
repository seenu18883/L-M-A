package com.library.app.controller;

import com.library.app.dto.AddressDTO;
import com.library.app.service.AddressService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/addresses")
public class AddressController {

    private static  final Logger logger = LoggerFactory.getLogger(AddressController.class);

    private AddressService addressService;


    @PostMapping("createAddress")
    // http://localhost:8080/api/addresses/createAddress
    public ResponseEntity<AddressDTO> addBook( @RequestBody AddressDTO addressDTO) {

        logger.info("Address Adding ...");
        AddressDTO savedPostalAddressDTO = addressService.createAddress(addressDTO);
        logger.info("Saved the PostalAddressDTO: {}",savedPostalAddressDTO);
        return new ResponseEntity<>(savedPostalAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("listAll")
    // http://localhost:8080/api/addresses/listAll

    public ResponseEntity<List<AddressDTO>> getAllAddresses(){

      List<AddressDTO>  allAddresses = addressService.getAllAddresses();
      return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }


    @GetMapping("{id}")

    // e. g.  http://localhost:8080/api/addresses/1

    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id){

      AddressDTO addressDTO =  addressService.getAddressById(id);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }


    @PatchMapping("updateAddress/{id}")
    // e.g. http://localhost:8080/api/addresses/updateAddress/1
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {

        addressDTO.setId(id);
        AddressDTO updatedAddress = addressService.updateAddress(addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }


    @DeleteMapping("deleteAddress/{id}")

    // e.g URL: http://localhost:8080/api/addresses/deleteAddress/3
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>("Address successfully deleted", HttpStatus.OK);
    }


}
