package com.library.app.dto;

import lombok.Data;

@Data
public class MemberDTO {


    // validations are added later
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private AddressDTO postalAddress; // Assuming you have a PostalAddressDTO
    private String email;
    private String phone;
    private String barcodeNumber;
    private String membershipStarted;
    private String membershipEnded;
    private Boolean isActive;
}
