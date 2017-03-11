/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.repository;

import br.com.mrrobot.domain.Category;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Alisson Diniz
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findByName(String name);

}