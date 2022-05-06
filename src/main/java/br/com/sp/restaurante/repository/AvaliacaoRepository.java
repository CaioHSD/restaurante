package br.com.sp.restaurante.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.restaurante.model.Avaliacao;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long>{

	public List<Avaliacao> findByRestauranteId(Long idRestaurante);
}
