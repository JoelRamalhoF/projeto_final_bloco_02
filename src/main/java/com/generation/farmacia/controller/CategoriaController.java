package com.generation.farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

	// Injeta o repositório responsável pelas operações da entidade Categoria
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Categoria>> getAll() {

		return ResponseEntity.ok(categoriaRepository.findAll());
	}

	// Busca um tema pelo id informado na URL
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id) {

		return categoriaRepository.findById(id)
				// Se encontrar a categoria, retorna 200 OK com o objeto no corpo
				.map(resposta -> ResponseEntity.ok(resposta))
				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Busca todas categorias com o tipo informado e Ignora diferença entre letras
	// maiúsculas e minúsculas
	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<Categoria>> getAllByTipo(@PathVariable String tipo) {

		return ResponseEntity.ok(categoriaRepository.findAllByTipoContainingIgnoreCase(tipo));

	}

	// Garante que o id será gerado automaticamente pelo banco
	// Isso evita que o cadastro tente reutilizar um id já existente
	@PostMapping
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {

		categoria.setId(null);
		// Salva a nova categoria e retorna 201 CREATED
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}

	@PutMapping
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {

		// Busca a categoria pelo id antes de atualizar
		// Se o id existir, salva a atualização
		return categoriaRepository.findById(categoria.getId())

				// Se encontrar, salva a categoria atualizada e retorna a resposta
				.map(resposta -> ResponseEntity.ok(categoriaRepository.save(categoria)))

				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		// Busca a categoria id antes de excluir
		Optional<Categoria> categoria = categoriaRepository.findById(id);

		// Se não encontrar, lança exceção com status 404 Not Found
		if (categoria.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Se encontrar, exclui a cateorigia do banco de dados
		categoriaRepository.deleteById(id);
	}
}
