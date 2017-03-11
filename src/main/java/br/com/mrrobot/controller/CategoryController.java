/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.controller;

import br.com.mrrobot.repository.CategoryRepository;
import br.com.mrrobot.to.CategoryTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alisson Diniz
 */
@RestController
public class CategoryController {
    
    @Autowired
    private CategoryRepository repository;
    
    @RequestMapping("category/list")
    public ResponseEntity<List<CategoryTO>> list() {
        return ResponseEntity.ok(
                StreamSupport.stream(this.repository.findAll().spliterator(), false)
                    .map(it -> new CategoryTO(it.getId(), it.getName()))
                    .collect(Collectors.toList()));
    }
    
}
