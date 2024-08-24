package com.app.service;

import com.app.dto.LoansDto;

import java.util.List;

public interface LoanService {

    public void createLoan(String mobileNumber);
    public LoansDto fetchloans(String mobileNumber);
    public LoansDto updateLoans(LoansDto dto);
    public  boolean deleteLaons(String mobileNumber);
    public List<LoansDto> getAllLoans();

}
