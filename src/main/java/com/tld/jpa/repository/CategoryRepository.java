package com.tld.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tld.entity.Category;



public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
