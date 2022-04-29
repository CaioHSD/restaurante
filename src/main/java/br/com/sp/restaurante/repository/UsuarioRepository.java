package br.com.sp.restaurante.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.restaurante.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{

	
}
