package br.com.sp.restaurante.restautaurante.controller;

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

import br.com.sp.restaurante.restautaurante.model.Administrador;
import br.com.sp.restaurante.restautaurante.repository.AdminRepository;
import br.com.sp.restaurante.restautaurante.util.HashUtil;

@Controller
public class AdminController {

	@Autowired
	private AdminRepository repository;

	@RequestMapping("formulario")
	public String form() {

		return "formAdm";

	}

	@RequestMapping(value = "salvarAdmin", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		// verifca se houveram erros na validação
		if (result.hasErrors()) {
			// adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "Verifique os campos...");

			// redireciona para o formulario
			return "redirect:formulario";

		}
		// variável para descobrir alteração ou inserção
		boolean alteracao = admin.getId() != null ? true : false;

		// verificar se a senha está vazia
		if (admin.getSenha().equals(HashUtil.hash(""))) {
			
			if (!alteracao) {
				// retira a parte antes do @ no e-mail
				String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));
				// "setar" a parte na senha do admin
				admin.setSenha(parte);

			}else {
				//buscar senha atual no banco
				String hash = repository.findById(admin.getId()).get().getSenha();
				//"setar" o hash na senha
				admin.setSenhaComHash(hash);
			}
		}

		try {
			// salva no banco de dados o adm
			repository.save(admin);
			// adiciona uma mensagem de sucesso
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. ID:" + admin.getId());

		} catch (Exception e) {
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar: " + e.getMessage());
		}

		return "redirect:formulario";

	}

	@RequestMapping("listaAdm/{page}")
	public String listaAdmin(Model model, @PathVariable("page") int page) {
		// cria um pageable informando os parâmeros da pagina

		// sort ordena, asc é de A a Z e nome é o tipo de ordenar
		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "nome"));

		// cria um page de Administrador através dos parametros passados ao repository
		Page<Administrador> pagina = repository.findAll(pageable);

		// adiciona a model á lista com os admins
		model.addAttribute("admins", pagina.getContent());

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

		// retorna para o html da lista
		return "listaAdm";

	}

	@RequestMapping("alterarAdm")
	public String alterar(Long id, Model model) {
		Administrador admin = repository.findById(id).get();
		model.addAttribute("admin", admin);
		return "forward:formulario";

	}

	@RequestMapping("excluirAdm")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listaAdm/1";
	}

}
