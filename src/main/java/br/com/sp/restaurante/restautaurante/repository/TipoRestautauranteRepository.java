package br.com.sp.restaurante.restautaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.restaurante.restautaurante.model.TipoRestautaurante;

public interface TipoRestautauranteRepository extends PagingAndSortingRepository<TipoRestautaurante, Long> {

	public List<TipoRestautaurante> findByPalavrasChaveLike(String rest);

	@Query("SELECT rest FROM TipoRestautaurante rest WHERE rest.nome LIKE %:r% OR rest.descricao LIKE %:r% OR rest.palavrasChave LIKE %:r% ")
	public List<TipoRestautaurante> buscarRest(@Param("r") String param);
}
