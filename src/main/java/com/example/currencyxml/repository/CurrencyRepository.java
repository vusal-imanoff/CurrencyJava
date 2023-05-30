package com.example.currencyxml.repository;

import com.example.currencyxml.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
}
