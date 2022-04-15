package br.com.sp.restaurante.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.restaurante.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long> {
	public Administrador findByEmailAndSenha(String email, String senha);
	
	
}
