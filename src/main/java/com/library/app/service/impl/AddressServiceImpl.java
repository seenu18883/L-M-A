package com.library.app.service.impl;

import com.library.app.controller.AddressController;
import com.library.app.dto.AddressDTO;
import com.library.app.entity.PostalAddress;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.mapper.AddressMapper;
import com.library.app.repository.AddressRepository;
import com.library.app.service.AddressService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static  final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

        private AddressRepository addressRepository;


    @Override
    public AddressDTO createAddress(AddressDTO postalAddressDTO) {

         logger.info("Trying to add Address:{}",postalAddressDTO);
        PostalAddress postalAddress = AddressMapper.mapToAddressEntity(postalAddressDTO);
         logger.info("Address entity after the mapping :{}",postalAddress);
        postalAddress = addressRepository.save(postalAddress);
        logger.info("The Address successfully saved in database :{}",postalAddress);
        return AddressMapper.mapToAddressDTO(postalAddress);
    }

    @Override
    public AddressDTO updateAddress(AddressDTO postalAddressDTO) {
        // 1. find existing address by id
        Optional<PostalAddress> addressOptional = addressRepository.findById(postalAddressDTO.getId());

        // 2. do partial update of the address (only non-null fields)
        PostalAddress addressToUpdate = addressOptional.orElseThrow(
                ()-> new ResourceNotFoundException("Address","ID", postalAddressDTO.getId()));

        updateAddressEntityFromDTO(addressToUpdate, postalAddressDTO); // create method here

        // 3. save updated book to database
        addressRepository.save(addressToUpdate);

        // 4. return bookDTO using mapper
        return AddressMapper.mapToAddressDTO(addressToUpdate);
    }

    public void updateAddressEntityFromDTO(PostalAddress addressToUpdate, AddressDTO postalAddressDTO) {

        if (postalAddressDTO.getStreetName() != null) {
            addressToUpdate.setStreetName(postalAddressDTO.getStreetName());
        }
        if (postalAddressDTO.getStreetNumber() != null) {
            addressToUpdate.setStreetNumber(postalAddressDTO.getStreetNumber());
        }
        if (postalAddressDTO.getZipCode() != null) {
            addressToUpdate.setZipCode(postalAddressDTO.getZipCode());
        }
        if (postalAddressDTO.getPlaceName() != null) {
            addressToUpdate.setPlaceName(postalAddressDTO.getPlaceName());
        }
        if (postalAddressDTO.getCountry() != null) {
            addressToUpdate.setCountry(postalAddressDTO.getCountry());
        }
        if (postalAddressDTO.getAdditionalInfo() != null) {
            addressToUpdate.setAdditionalInfo(postalAddressDTO.getAdditionalInfo());
        }

    }

    @Override
    public List<AddressDTO> getAllAddresses() {
       List<PostalAddress>  postalAddresses = addressRepository.findAll();

        return postalAddresses.stream()
                .map(AddressMapper::mapToAddressDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {

//        Optional<PostalAddress>  optionalPostalAddress = addressRepository.findById(id);
//        PostalAddress postalAddress = optionalPostalAddress.get();
        PostalAddress postalAddress = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address","ID",id)
        );
        return AddressMapper.mapToAddressDTO(postalAddress);
    }

    @Override
    public void deleteAddress(Long id) {

        if(!addressRepository.existsById(id)){
            throw  new ResourceNotFoundException("Book" , "ID",id);
        }
        addressRepository.deleteById(id);
    }
}
