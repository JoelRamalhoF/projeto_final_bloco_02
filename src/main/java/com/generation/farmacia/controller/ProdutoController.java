package com.generation.farmacia.controller;

import java.math.BigDecimal;
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

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ProdutoController {
	// Injeta o repositório responsável pelas operações da entidade produto
	@Autowired
	private ProdutoRepository produtoRepository;
	// injeta o repositório de Categoria para validar se a categoria informada
	// existe
	@Autowired
	private CategoriaRepository categoriaRepository;

	// Retorna todas os produtos cadastradas no banco
	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {

		return ResponseEntity.ok(produtoRepository.findAll());

	}

	// Busca um produto pelo id informado na URL
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {

		// Busca um produto pelo id informado na URL

		return produtoRepository.findById(id)

				// Se encontrar o produto retorna 200 OK com o objeto no corpo
				.map(resposta -> ResponseEntity.ok(resposta))

				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.notFound().build());
	}

	// Busca todas produtos com o nome informado e Ignora diferença entre letras
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getAllByNome(@PathVariable String nome) {

		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

// Busca todas produtos com o preço maior que o informado e ordena pelo preço
	@GetMapping("preco_maior/{preco}")
	public ResponseEntity<List<Produto>> getAllPrecoMaiorQue(@PathVariable BigDecimal preco) {
		return ResponseEntity.ok(produtoRepository.findAllByPrecoGreaterThanOrderByPreco(preco));

	}

// Busca todas produtos com o preço menor que o informado e ordena pelo preço
	@GetMapping("/preco_menor/{preco}")
	public ResponseEntity<List<Produto>> getAllPrecoMenorQue(@PathVariable BigDecimal preco) {
		return ResponseEntity.ok(produtoRepository.findAllByPrecoLessThanOrderByPrecoDesc(preco));
	}

// Garante que o id será gerado automaticamente pelo banco
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId())) {

			produto.setId(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		}
		// Se a categoria informada não existir, retorna erro 400 Bad Request
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A Categoria não existe!", null);
	}
	// Atualiza um produto já existente

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		// Primeiro verifica se o produto existe
		if (produtoRepository.existsById(produto.getId())) {

			// Depois verifica se a categoria informada tambem existe
			if (categoriaRepository.existsById(produto.getCategoria().getId())) {

				// Se ambos existirem, salva a atualização e retorna 200 OK
				return ResponseEntity.ok(produtoRepository.save(produto));

			}
			// Se a categoria nao existir, retorna erro 400 Bad Request
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
		}
		// Se o produto nao existir, retorna 404 Not Found
		return ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		// Busca o produto pelo id antes de excluir
		Optional<Produto> produto = produtoRepository.findById(id);

		// Se não encontrar, retorna 404 Not Found
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Se encontrar, exclui o produto
		produtoRepository.deleteById(id);

	}
}
