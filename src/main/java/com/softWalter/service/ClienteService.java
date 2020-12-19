package com.softWalter.service;

import com.softWalter.model.Cliente;
import com.softWalter.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private ClienteRepository repository;

    @Autowired
    public ClienteService(ClienteRepository repository){
        this.repository = repository;
    }

//    /**
//     * Metodo setRepository usado para injetar a dependencia.
//     * @param repository
//     */
//    public void setRepository(ClienteRepository repository) {
//        this.repository = repository;
//    }

    public void salvarCliente(Cliente cliente){
        validarCliente(cliente);
        this.repository.persistir(cliente);
    }
    public  void validarCliente(Cliente cliente){
        //validar Cliente
    }
}
