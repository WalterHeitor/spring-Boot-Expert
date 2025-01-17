package com.softWalter.restApi.controller;

import com.softWalter.model.Cliente;
import com.softWalter.repository.ClientesRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/clientes")
public class Clientecontroller {

    @RequestMapping(
            value = {"/hello/{nome}", "/api/hello"},
            method = RequestMethod.GET,
            consumes = {"aplication/json", "application/xml"},
            produces = {"aplication/json", "application/xml"}
    )
    @ResponseBody
    public String hello(@PathVariable("nome") String nomeCliente){
        return String.format("Hello %s", nomeCliente);
    }



    private ClientesRepository clientesRepository;
    public Clientecontroller(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity getClienteById(@PathVariable("id") Long id){
        Optional<Cliente> cliente = clientesRepository.findById(id);

        if (cliente.isPresent()){
            return ResponseEntity.ok(cliente.get());
        }

    return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseBody
    public  ResponseEntity save(@RequestBody Cliente cliente){
        Cliente clienteSalvo = clientesRepository.save(cliente);
        return ResponseEntity.ok(clienteSalvo);

    }
    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity deletar(@PathVariable Long id){
        Optional<Cliente> cliente = clientesRepository.findById(id);

        if (cliente.isPresent()){
            clientesRepository.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.notFound().build();
    }
    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestBody Cliente cliente){
        return clientesRepository
                .findById(id)
                .map(clienteExistente ->{
                        cliente.setId(clienteExistente.getId());
                        clientesRepository.save(cliente);
                        return ResponseEntity.noContent().build();
    }).orElseGet(()->ResponseEntity.notFound().build());

    }
    @GetMapping
    public ResponseEntity find(Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING
                );
        Example example = Example.of(filtro, matcher);
        List<Cliente>lista = clientesRepository.findAll(example);
        return ResponseEntity.ok(lista);
    }
}
