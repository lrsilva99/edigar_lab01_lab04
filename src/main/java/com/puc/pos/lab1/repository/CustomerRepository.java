package com.puc.pos.lab1.repository;

import com.puc.pos.lab1.domain.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Spring Data MongoDB repository for the Customer entity.
 */
@SuppressWarnings("unused")
public interface CustomerRepository extends MongoRepository<Customer,String> {

    /**
     *  Criado por: Leonardo Ribeiro Silva
     *  Query elaborada para filtrar as colunas do documento no mongo
     *  @param name, site,cnpj,address
     */
    @Query("{ 'name' : ?0 , 'site' : ?1 , 'cnpj' : ?2 , 'address' : ?3 }")
    Page<Customer> filterFullColumn(String name, String site,String cnpj,String address, Pageable pageable);
}
