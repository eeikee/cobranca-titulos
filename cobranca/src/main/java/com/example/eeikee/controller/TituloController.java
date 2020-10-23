package com.example.eeikee.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring5.expression.Mvc;

import com.example.eeikee.model.StatusTitulo;
import com.example.eeikee.model.Titulo;
import com.example.eeikee.repository.Titulos;
import com.example.eeikee.repository.filter.TituloFilter;
import com.example.eeikee.service.CadastroTituloService;

import jdk.jshell.ErroneousSnippet;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroDeTitulo";
	
	@Autowired
	private CadastroTituloService cadastroTituloService;

	@RequestMapping("/novo")
	public ModelAndView novoTitulo() {
		ModelAndView mView = new ModelAndView(CADASTRO_VIEW);
		mView.addObject(new Titulo());
		return mView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors error, RedirectAttributes redirectAttributes) {
		if (error.hasErrors()) {
			return CADASTRO_VIEW;
		}
		try {
			cadastroTituloService.salvar(titulo);
			redirectAttributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso!");
			return "redirect:/titulos/novo";
		} catch (DataIntegrityViolationException e) {
			error.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}	
	}

	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro")TituloFilter filtro) {
		
	List<Titulo> todosTitulos = cadastroTituloService.filtrar(filtro);
		ModelAndView mView = new ModelAndView("PesquisaTitulos");
		mView.addObject("titulos", todosTitulos);
		return mView;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mView = new ModelAndView(CADASTRO_VIEW);
		mView.addObject(titulo);
		return mView;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
	cadastroTituloService.excluir(codigo);
	attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
	return "redirect:/titulos?descricao=";
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return cadastroTituloService.receber(codigo);
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}
}
