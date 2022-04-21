package br.com.sp.restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.restaurante.model.TipoRestaurante;

public interface TipoRestauranteRepository extends PagingAndSortingRepository<TipoRestaurante, Long> {

	public List<TipoRestaurante> findByPalavrasChaveLike(String rest);

	@Query("SELECT rest FROM TipoRestaurante rest WHERE rest.nome LIKE %:r% OR rest.descricao LIKE %:r% OR rest.palavrasChave LIKE %:r% ")
	public List<TipoRestaurante> buscarRest(@Param("r") String param);
	
	public List<TipoRestaurante> findAllByOrderByNomeAsc();
	
	
}
