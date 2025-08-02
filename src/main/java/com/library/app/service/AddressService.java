package com.library.app.service;

import com.library.app.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO postalAddressDTO);

    AddressDTO updateAddress(AddressDTO postalAddressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long id);

    void deleteAddress(Long id);


}
