package br.com.sp.restaurante.restautaurante.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.restaurante.restautaurante.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long> {
	
	
}
