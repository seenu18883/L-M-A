package com.library.app.service;

import com.library.app.dto.RegisterDTO;

import java.util.List;

public interface RegisterService {

   RegisterDTO createRegister(RegisterDTO registerDTO);

   List<RegisterDTO> getAllRegisters();

   RegisterDTO getRegisterById(Long id);

   RegisterDTO updateRegister(RegisterDTO registerDTO);

    void deleteRegister(Long id);

    List<RegisterDTO> getRegisterByMemberId(Long memberId);

   List<RegisterDTO> getRegisterByBookId(Long bookId);
}
