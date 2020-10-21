package com.example.eeikee.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring5.expression.Mvc;

import com.example.eeikee.model.StatusTitulo;
import com.example.eeikee.model.Titulo;
import com.example.eeikee.repository.Titulos;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	@Autowired
	private Titulos titulos;

	@RequestMapping("/novo")
	public ModelAndView novoTitulo() {
		ModelAndView mView = new ModelAndView("CadastroDeTitulo");
		mView.addObject(new Titulo());
		return mView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors error, RedirectAttributes redirectAttributes) {
		if (error.hasErrors()) {
			return "CadastroDeTitulo";
		}
		titulos.save(titulo);
		redirectAttributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso!");
		return "redirect:/titulos/novo";
	}

	@RequestMapping
	public ModelAndView pesquisar() {
		ModelAndView mView = new ModelAndView("PesquisaTitulos");
		List<Titulo> todosTitulos = titulos.findAll();
		mView.addObject("titulos", todosTitulos);
		return mView;
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}
}
