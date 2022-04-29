package br.com.sp.restaurante.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.restaurante.model.Avaliacao;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long>{

}
