package com.kwchaiwat.dev.springbootkotlin.controller.bank

import com.kwchaiwat.dev.springbootkotlin.controller.Controller
import com.kwchaiwat.dev.springbootkotlin.model.Bank
import com.kwchaiwat.dev.springbootkotlin.model.BankRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/banks")
class BankController(
    @Autowired private val bankRepository: BankRepository
) : Controller {

    @GetMapping
    fun getBanks(): Collection<Bank> = bankRepository.findAll()

    @GetMapping("/{accountNumber}")
    fun getBank(@PathVariable accountNumber: String): Bank {
        return bankRepository.findByAccountNumber(accountNumber)
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank): Bank {
        if (bankRepository.findAll().any{it.accountNumber == bank.accountNumber}) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exits.")
        }
        bankRepository.save(bank)
        return bank
    }

    @PatchMapping
    fun updateBank(@RequestBody bank: Bank): Bank {
        val currentBank = bankRepository.findAll().firstOrNull { it.accountNumber == bank.accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")
        bankRepository.delete(currentBank)
        bankRepository.save(bank)
        return bank
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable accountNumber: String) {
        val banks: Bank = bankRepository.findByAccountNumber(accountNumber)
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
        bankRepository.delete(banks)
    }

}