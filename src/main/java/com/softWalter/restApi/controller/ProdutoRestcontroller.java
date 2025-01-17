package com.softWalter.restApi.controller;

import com.softWalter.model.Produto;
import com.softWalter.repository.ProdutosRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoRestcontroller {

    private ProdutosRepository produtosRepository;
    public ProdutoRestcontroller(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }
    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody @Valid Produto produto){
        return produtosRepository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody @Valid Produto produto){
        produtosRepository
                .findById(id)
                .map(produto1 -> {
                    produto.setId(produto1.getId());
                    produtosRepository.save(produto);
                    return produto;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Produto não encontrado."));
    }
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id){
        produtosRepository.findById(id)
                .map(produto -> {
                    produtosRepository.delete(produto);
                    return Void.TYPE;
                })
                .orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado."));
    }
    @GetMapping("{id}")
    public Produto getById(@PathVariable Long id){
        return produtosRepository.findById(id)
                .orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND,
                        "Produto nao encontrado"));
    }
    @GetMapping
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching().withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro,matcher);
        return produtosRepository.findAll(example);
    }

}
