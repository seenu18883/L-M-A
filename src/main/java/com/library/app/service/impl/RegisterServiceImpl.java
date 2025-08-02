package com.library.app.service.impl;

import com.library.app.dto.RegisterDTO;
import com.library.app.entity.CheckoutRegister;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.mapper.RegisterMapper;
import com.library.app.repository.CheckoutRegisterRepository;
import com.library.app.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    @Value("${library.loanPeriodInDays}")
    private int loanPeriodInDays;

    @Value("${library.overdueFineRate}")
    private double overdueFineRate;

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final RegisterMapper registerMapper;
    private final CheckoutRegisterRepository checkoutRegisterRepository;

    @Override
    public RegisterDTO createRegister(RegisterDTO registerDTO) {

        logger.info("Trying to add Register...");

        CheckoutRegister checkoutRegister = registerMapper.mapToCheckoutRegistryEntity(registerDTO);
        logger.info("Register entity after the mapping :{}",checkoutRegister);
        // calculate due Date

        LocalDate dueDate = calculateDueDate(checkoutRegister.getCheckoutDate());
        checkoutRegister.setDueDate(dueDate);

        checkoutRegister =  checkoutRegisterRepository.save(checkoutRegister);
        logger.info("The Register successfully saved in database :{}",checkoutRegister);
        return registerMapper.mapToRegisterDTO(checkoutRegister);
    }

    @Override
    public List<RegisterDTO> getAllRegisters() {

     List<CheckoutRegister>  checkoutRegisters = checkoutRegisterRepository.findAll();
        return checkoutRegisters.stream()
                .map(registerMapper::mapToRegisterDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RegisterDTO getRegisterById(Long id) {
//
//      Optional<CheckoutRegister> checkoutRegisterOptional = checkoutRegisterRepository.findById(id);
//      CheckoutRegister checkoutRegister = checkoutRegisterOptional.get();
        CheckoutRegister checkoutRegister = checkoutRegisterRepository.findById(id).orElseThrow(

                ()->new ResourceNotFoundException("CheckoutRegister","ID",id)
        );
        return registerMapper.mapToRegisterDTO(checkoutRegister);
    }

    @Override
    public RegisterDTO updateRegister(RegisterDTO registerDTO) {

        // find existing register by id
      Optional<CheckoutRegister>  checkoutRegisterOptional = checkoutRegisterRepository.findById(registerDTO.getId());
      CheckoutRegister checkoutRegisterToUpdate = checkoutRegisterOptional.orElseThrow(

              ()->new ResourceNotFoundException("CheckoutRegister","ID",registerDTO.getId())
      );

        // do partial update

        updateCheckoutRegisterFromDTO(checkoutRegisterToUpdate, registerDTO); // create method here

        // calculate overdue find
        calculateOverdueFine(checkoutRegisterToUpdate);

        // save updated register to DB

        CheckoutRegister updateCheckRegister = checkoutRegisterRepository.save(checkoutRegisterToUpdate);

        // return register DTO using mapper
        return registerMapper.mapToRegisterDTO(updateCheckRegister);
    }

    @Override
    public void deleteRegister(Long id) {

        if(!checkoutRegisterRepository.existsById(id)){
            throw  new ResourceNotFoundException("Book" , "ID",id);
        }
        checkoutRegisterRepository.deleteById(id);

    }

    @Override
    public List<RegisterDTO> getRegisterByMemberId(Long memberId) {
       List<CheckoutRegister> checkoutRegisters = checkoutRegisterRepository.findByMemberId(memberId);
        return checkoutRegisters.stream()
                .map(registerMapper::mapToRegisterDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisterDTO> getRegisterByBookId(Long bookId) {

        List<CheckoutRegister> checkoutRegisters = checkoutRegisterRepository.findByBookId(bookId);
        return checkoutRegisters.stream()
                .map(registerMapper::mapToRegisterDTO)
                .collect(Collectors.toList());
    }

    private void calculateOverdueFine(CheckoutRegister checkoutRegisterToUpdate) {

        if (checkoutRegisterToUpdate.getReturnDate() != null && checkoutRegisterToUpdate.getReturnDate().isAfter(checkoutRegisterToUpdate.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(checkoutRegisterToUpdate.getDueDate(), checkoutRegisterToUpdate.getReturnDate());

            // overdue fine = daysOverdue *overdueFineRate

            double overdueFine = daysOverdue * overdueFineRate;
            checkoutRegisterToUpdate.setOverdueFine(overdueFine);
        }
    }
    private void updateCheckoutRegisterFromDTO(CheckoutRegister checkoutRegisterToUpdate, RegisterDTO registerDTO) {

        // the agent can either prolong the book or record the of the book

        if(registerDTO.getDueDate() !=null)
            checkoutRegisterToUpdate.setDueDate(LocalDate.parse( registerDTO.getDueDate()));

        if(registerDTO.getReturnDate() !=null)
            checkoutRegisterToUpdate.setReturnDate(LocalDate.parse( registerDTO.getReturnDate()));
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate) {

       return checkoutDate.plusDays(loanPeriodInDays);

    }
}
