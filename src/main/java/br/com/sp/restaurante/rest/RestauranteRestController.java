package br.com.sp.restaurante.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.restaurante.model.Restaurante;
import br.com.sp.restaurante.model.TipoRestaurante;
import br.com.sp.restaurante.repository.RestauranteRepository;
import br.com.sp.restaurante.repository.TipoRestauranteRepository;

@RestController
@RequestMapping("/api/restaurante")
public class RestauranteRestController {
		
		@Autowired
		private RestauranteRepository repository;
		
		@RequestMapping(value = "", method = RequestMethod.GET)
		public Iterable<Restaurante> getRestaurantes(){
			return repository.findAll();
		}
		
		@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<Restaurante> getRestaurante(@PathVariable("id") Long idRestaurante){
			//tenta buscar o restaurante no repository
			Optional<Restaurante> optional = repository.findById(idRestaurante);
			//se o restaurante existir
			if(optional.isPresent()) {
				return ResponseEntity.ok(optional.get());
			}else {
				return ResponseEntity.notFound().build();
			}
			
		}
		
		@RequestMapping(value = "/tipo/id", method = RequestMethod.GET)
		public Iterable<TipoRestaurante> buscaTipo(@PathVariable("id") Long idTipo){
			return repository.findByTipoId(idTipo);
			
		}
		
		
}
