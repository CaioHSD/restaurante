package br.com.sp.restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.restaurante.model.Restaurante;
import br.com.sp.restaurante.model.TipoRestaurante;

public interface RestauranteRepository extends PagingAndSortingRepository<Restaurante, Long>{
	@Query("SELECT restaurante FROM Restaurante restaurante WHERE restaurante.nome LIKE %:r% OR restaurante.endereco LIKE %:r% OR restaurante.descricao LIKE %:r% ")
	public List<Restaurante> buscarRestaurante(@Param("r") String param);
	
	public List<TipoRestaurante> findByTipoId (Long idTipo);
	
}
