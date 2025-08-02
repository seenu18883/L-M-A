package com.library.app.mapper;

import com.library.app.dto.AddressDTO;
import com.library.app.entity.PostalAddress;

public class AddressMapper {


    public static AddressDTO mapToAddressDTO(PostalAddress postalAddress) {
        AddressDTO dto = new AddressDTO();
        dto.setId(postalAddress.getId());
        dto.setStreetName(postalAddress.getStreetName());
        dto.setStreetNumber(postalAddress.getStreetNumber());
        dto.setZipCode(postalAddress.getZipCode());
        dto.setPlaceName(postalAddress.getPlaceName());
        dto.setCountry(postalAddress.getCountry());
        dto.setAdditionalInfo(postalAddress.getAdditionalInfo());
        return dto;
    }

    public static PostalAddress mapToAddressEntity(AddressDTO dto) {
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setId(dto.getId());
        postalAddress.setStreetName(dto.getStreetName());
        postalAddress.setStreetNumber(dto.getStreetNumber());
        postalAddress.setZipCode(dto.getZipCode());
        postalAddress.setPlaceName(dto.getPlaceName());
        postalAddress.setCountry(dto.getCountry());
        postalAddress.setAdditionalInfo(dto.getAdditionalInfo());
        return postalAddress;
    }
}
