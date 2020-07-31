package com.howtodoinjava.demo.controller;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.demo.exception.RecordNotFoundException;
import com.howtodoinjava.demo.model.Aluno;
import com.howtodoinjava.demo.repository.AlunoRepository;

@RestController
@RequestMapping(value = "/aluno-management", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class AlunoRESTController {
	
	// https://howtodoinjava.com/spring-restful/spring-rest-crud-jpa-example/
	// ACESSAR A APLICAÇÃO : http://localhost:8080/SpringRestExample-0.0.1-SNAPSHOT/api/rest/aluno-management/alunos/
	// USAR POST, GET, PUT, DELETE pra interagir com a aplicação

	@Autowired
	private AlunoRepository repository;

	public AlunoRepository getRepository() {
		return repository;
	}

	public void setRepository(AlunoRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/alunos")
	public List<Aluno> getAllAlunos() {
		return repository.findAll();
	}

	@PostMapping("/alunos")
	Aluno createOrSaveAluno(@RequestBody Aluno newAluno) {
		return repository.save(newAluno);
	}
	
	@GetMapping("/alunos/{id}")
	Aluno getAlunoById(@PathVariable 
							 @Min(value = 1, message = "id must be greater than or equal to 1") 
							 @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id)
	{
	    return repository.findById(id)
	            .orElseThrow(() -> new RecordNotFoundException("Aluno id '" + id + "' does no exist"));
	}

	@PutMapping("/alunos/{id}")
	Aluno updateAluno(@RequestBody Aluno newAluno, @PathVariable Long id) {

		return repository.findById(id).map(aluno -> {
			aluno.setNome(newAluno.getNome());
			aluno.setMatricula(newAluno.getMatricula());
			aluno.setEmail(newAluno.getEmail());
			return repository.save(aluno);
		}).orElseGet(() -> {
			newAluno.setId(id);
			return repository.save(newAluno);
		});
	}

	@DeleteMapping("/alunos/{id}")
	void deleteAluno(@PathVariable Long id) {
		repository.deleteById(id);
	}
}