package br.com.sp.restaurante.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.sp.restaurante.model.Restaurante;
import br.com.sp.restaurante.model.TipoRestaurante;
import br.com.sp.restaurante.repository.RestauranteRepository;
import br.com.sp.restaurante.repository.TipoRestauranteRepository;
import br.com.sp.restaurante.util.FireBaseUtil;

@Controller
public class RestauranteController {
	@Autowired
	private TipoRestauranteRepository repTipo;
	@Autowired
	private RestauranteRepository repository;
	@Autowired
	private FireBaseUtil fireUtil;
	
	
	@RequestMapping("formularioRestaurante")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "formRestaurante";

		
	}
	@RequestMapping(value = "salvarRestaurante" , method = RequestMethod.POST)
	public String salvarRestaurante(Restaurante restaurante, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		//String para armazenar as URL´s
		String fotos = restaurante.getFotos();
		//percorre cada aquivo no vetor
		for(MultipartFile arquivo : fileFotos) {
			//verifica se o arquivo existe
			if(arquivo.getOriginalFilename().isEmpty()) {
				// vai para o próxmo arquivo
				continue;
				
			}
			try {
				//faz o upload e guarda a URL na String fotos
				fotos += fireUtil.upload(arquivo)+";";
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}
		
		//guarda na vaiavel restaurante as fotos
		restaurante.setFotos(fotos);
		//salva no BD
		repository.save(restaurante);	
		
		return "redirect:formularioRestaurante";
		
		
	}
	@RequestMapping("listaRestaurante/{page}")
	public String listaRestaurante(Model model,  @PathVariable("page") int page) {
		// cria um pageable informando os parâmeros da pagina

				// sort ordena, asc é de A a Z e nome é o tipo de ordenar
				PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "nome"));

				// cria um page de Administrador através dos parametros passados ao repository
				Page<Restaurante> pagina = repository.findAll(pageable);

				// adiciona a model á lista com os admins
				model.addAttribute("restaurantes", pagina.getContent());

				// variável para o total de paginas
				int totalPages = pagina.getTotalPages();

				// cria um ,ist de inteiros para armazenar os numeros das paginas
				List<Integer> numPaginas = new ArrayList<Integer>();

				// preencher o list com paginas
				for (int i = 1; i <= totalPages; i++) {

					// adiciona a pagina ao list
					numPaginas.add(i);

				}

				// adiciona os valores á model
				model.addAttribute("numPaginas", numPaginas);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("pagAtual", page);

		return "listaRestaurante";

	}
	@RequestMapping(value = "buscarRestaurante", method = RequestMethod.GET)
	public String buscarRestaurante(String restautaurante, Model model) {
		model.addAttribute("restautaurante", repository.buscarRestaurante(restautaurante));
		return "listaRestaurante";

	}
	

	@RequestMapping("alterarRestaurante")
	public String alterarRestaurante(Long id, Model model) {
		System.out.println(id);
		Restaurante restaurante= repository.findById(id).get();
		model.addAttribute("restaurante", restaurante);
		return "forward:formularioRestaurante";

	}

	@RequestMapping("excluirRestaurante")
	public String excluirRestaurante(Long idRestaurante) {
		Restaurante rest = repository.findById(idRestaurante).get();
		if(rest.getFotos().length() > 0) {
			for(String foto : rest.verFotos()) {
				fireUtil.deletar(foto);
			}
			
		}
		repository.delete(rest);
		return "redirect:listaRestaurante/1";
		
	}
	@RequestMapping("excluirFotos")
	public String excluirFotos(Long idRest, int numFoto, Model model) {
		//busca o restaurante
		Restaurante rest = repository.findById(idRest).get();
		//busa a URL da foto
		String urlFoto = rest.verFotos()[numFoto];
		//deleta a foto
		fireUtil.deletar(urlFoto);
		//remove a URL do atributo fotos
		rest.setFotos(rest.getFotos().replace(urlFoto+";", ""));
		//salva no banco
		repository.save(rest);
		//coloca o rest na model
		model.addAttribute("restaurante", rest);
		
		return "forward:formularioRestaurante";
		
	}

	
	
	 
	
}
