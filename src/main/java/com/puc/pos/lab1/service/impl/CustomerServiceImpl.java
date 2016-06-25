package com.puc.pos.lab1.service.impl;

import com.puc.pos.lab1.service.CustomerService;
import com.puc.pos.lab1.domain.Customer;
import com.puc.pos.lab1.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Customer.
 */
@Service
public class CustomerServiceImpl implements CustomerService{

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Inject
    private CustomerRepository customerRepository;

    /**
     * Save a customer.
     *
     * @param customer the entity to save
     * @return the persisted entity
     */
    public Customer save(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        Customer result = customerRepository.save(customer);
        return result;
    }

    /**
     *  Get all the customers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Customer> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        Page<Customer> result = customerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one customer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Customer findOne(String id) {
        log.debug("Request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        return customer;
    }

    /**
     *  Delete the  customer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.delete(id);
    }

    /**
     *  Criado por: Leonardo Ribeiro Silva
     *  Objetivo  : Consultar o documento com todos os parametro no mongo DB.
     *
     *  @param name, site,cnpj,address
     */
    public Page<Customer> filterFullColumn(String name, String site,String cnpj,String address, Pageable pageable) {
        log.debug("Retornar um usuário específico");
        Page<Customer> result = customerRepository.filterFullColumn(name,site,cnpj,address,pageable);
        return result;
    }

    /**
     * Criado por: Leonardo Ribeiro Silva
     * Objetivo  : Criar paginação dos dados no ato da busca
     *
     * @param from
     * @param to
     */
    @Override
    public List<Customer> findFromTo(int from, int to) {
        List<Customer> cutomerList =  customerRepository.findAll();
        int compara = 1;
        ArrayList<Customer>  linhaRemover = new ArrayList<Customer>();
        // Compara os objetos que devem ser removidos do List
        for (int i = 0; i < cutomerList.size(); i++) {
            if (from <=compara )
                if (to >=compara){}
                else
                    linhaRemover.add(cutomerList.get(i));
            compara ++;
        }
        // Remover os objetos do list
        for (int i = 0; i < linhaRemover.size() ; i++) {
            cutomerList.remove(linhaRemover.get(i));
        }
        // Limpar memória
        linhaRemover = null;
        return cutomerList;
    }
}
