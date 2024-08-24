package com.app.service.impl;

import com.app.constants.LoanConstants;
import com.app.dto.LoansDto;
import com.app.entity.Loans;
import com.app.exception.ResourceAlreadyExistsException;
import com.app.exception.ResourceNotFoundException;
import com.app.mapper.LoansMapper;
import com.app.repository.LoanRepository;
import com.app.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;


    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> loans = repository.findByMobileNumber(mobileNumber);

        if(loans.isPresent())
        {
            throw  new ResourceAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
        }
        else {
            repository.save(createNewLoans(mobileNumber));
        }
    }

    @Override
    public LoansDto fetchloans(String mobileNumber) {
        Loans loans = repository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Loans not found with mobile number : "+mobileNumber));

        return LoansMapper.mapToLoanDto(loans);
    }

    @Override
    public LoansDto updateLoans(LoansDto dto) {
       Loans loans = repository.findByMobileNumber(dto.getMobileNumber())
               .orElseThrow(()->new ResourceNotFoundException("Loans not found with mobile number : "+dto.getMobileNumber()));

        loans.setLoanNumber(dto.getLoanNumber());
        loans.setLoanType(dto.getLoanType());
        loans.setMobileNumber(dto.getMobileNumber());
        loans.setTotalLoan(dto.getTotalLoan());
        loans.setAmountPaid(dto.getAmountPaid());
        loans.setOutstandingAmount(dto.getOutstandingAmount());

        return LoansMapper.mapToLoanDto(repository.save(loans));
    }

    @Override
    public boolean deleteLaons(String mobileNumber) {
        boolean isDelete  = false;
        Loans loans = repository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Loans not found with mobile number : "+mobileNumber));
        repository.delete(loans);
        isDelete=true;
        return isDelete;
    }

    @Override
    public List<LoansDto> getAllLoans() {
        List<Loans> loansList = repository.findAll();
        List<LoansDto> loansDtos = loansList.stream().map(loans -> LoansMapper.mapToLoanDto(loans)).collect(Collectors.toList());
        return loansDtos;
    }


    private Loans createNewLoans(String mobileNumber)
    {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoanConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoanConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoanConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
