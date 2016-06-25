package com.puc.pos.lab1.service;

import com.puc.pos.lab1.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Customer.
 */
public interface CustomerService {

    /**
     * Save a customer.
     *
     * @param customer the entity to save
     * @return the persisted entity
     */
    Customer save(Customer customer);

    /**
     *  Get all the customers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Customer> findAll(Pageable pageable);

    /**
     *  Get the "id" customer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Customer findOne(String id);

    /**
     *  Delete the "id" customer.
     *
     *  @param id the id of the entity
     */
    void delete(String id);

    /**
     *  Criado por: Leonardo Ribeiro Silva
     *  Objetivo  : Consultar o documento com todos os parametro no mongo DB.
     *
     *  @param name
     *  @param  site
     *  @param cnpj
     *  @param address
     */
    Page<Customer> filterFullColumn(String name, String site,String cnpj,String address, Pageable pageable);

    /**
     *  Criado por: Leonardo Ribeiro Silva
     *  Objetivo  : Criar paginação dos dados no ato da busca
     *
     *  @param from
     *  @param to
     */
    List<Customer> findFromTo(int from, int to);
}
