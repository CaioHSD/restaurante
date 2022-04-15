package br.com.sp.restaurante.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.com.sp.restaurante.model.TipoRestaurante;
import br.com.sp.restaurante.repository.TipoRestauranteRepository;

@Controller
public class TipoRestauranteController {

	@Autowired
	private TipoRestauranteRepository repository;

	@RequestMapping("formularioRest")
	public String form() {

		return "formRest";

	}

	@RequestMapping(value = "salvarRest", method = RequestMethod.POST)
	public String salvarRest(@Valid TipoRestaurante rest, BindingResult result, RedirectAttributes attr) {
	
		try {
			// salva no banco de dados o adm
			repository.save(rest);
			// adiciona uma mensagem de sucesso
			attr.addFlashAttribute("mensagemSucesso", "Restaurante cadastrado com sucesso. ID:" + rest.getId());

		} catch (Exception e) {
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar o restaurante: " + e.getMessage());
		}

		return "redirect:formularioRest";

	}

	@RequestMapping("listaRest/{page}")
	public String lista(Model model,  @PathVariable("page") int page) {
		// cria um pageable informando os parâmeros da pagina

				// sort ordena, asc é de A a Z e nome é o tipo de ordenar
				PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "nome"));

				// cria um page de Administrador através dos parametros passados ao repository
				Page<TipoRestaurante> pagina = repository.findAll(pageable);

				// adiciona a model á lista com os admins
				model.addAttribute("restautaurantes", pagina.getContent());

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

		return "listaRest";

	}
	
	@RequestMapping(value = "buscarRest", method = RequestMethod.GET)
	public String buscarRest(String restautaurante, Model model) {
		model.addAttribute("restautaurantes", repository.buscarRest(restautaurante));
		return "listaRest";

	}

	@RequestMapping("alterarRest")
	public String alterarRest(Long id, Model model) {
		TipoRestaurante rest = repository.findById(id).get();
		model.addAttribute("rest", rest);
		return "forward:formularioRest";

	}

	@RequestMapping("excluirRest")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listaRest/1";
	}

}
