package com.softWalter.restApi.controller;

import com.softWalter.model.Cliente;
import com.softWalter.repository.ClientesRepository;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/apirest/clientes")
@Api("Api Clientes")
public class ClienteRestcontroller {

    @RequestMapping(
            value = {"/hello/{nome}", "/api/hello"},
            method = RequestMethod.GET,
            consumes = {"aplication/json", "application/xml"},
            produces = {"aplication/json", "application/xml"}
    )

    public String hello(@PathVariable("nome") String nomeCliente){
        return String.format("Hello %s", nomeCliente);
    }



    private ClientesRepository clientesRepository;
    public ClienteRestcontroller(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }
    @GetMapping("{id}")
    @ApiOperation("Obter detalhes do cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado!"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado!")
    })
    public Cliente getClienteById(@PathVariable
                                              @ApiParam("Id do Cliente") Long id){
        return clientesRepository
                .findById(id)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                        ));


    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salvar um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com Sucesso!"),
            @ApiResponse(code = 400, message = "Erro de Validação!")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clientesRepository.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletar(@PathVariable Long id){
        clientesRepository.findById(id)
                    .map(cliente -> {
                        clientesRepository.delete(cliente);
                        return cliente;
                    })
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "cliente não encontrado"));
    }
    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id,
                                 @RequestBody @Valid Cliente cliente){
        clientesRepository
                .findById(id)
                .map(clienteExistente ->{
                        cliente.setId(clienteExistente.getId());
                        clientesRepository.save(cliente);
                        return cliente;
                        })
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "cliente não encontrado"));

    }
    @GetMapping()
    public List<Cliente> find(Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return clientesRepository.findAll(example);
    }
}
