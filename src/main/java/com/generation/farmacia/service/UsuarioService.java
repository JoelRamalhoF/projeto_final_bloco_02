package com.generation.farmacia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioLogin;
import com.generation.farmacia.repository.UsuarioRepository;
import com.generation.farmacia.security.JwtService;

// Classe de serviço para gerenciar operações relacionadas a usuários, como cadastro, atualização e autenticação.
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> getById(Long id) {
		return usuarioRepository.findById(id);
	}
	// Método para cadastrar um novo usuário. Verifica se o usuário já existe e se a idade é válida antes de salvar.
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

	    if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
	        return Optional.empty();
	    }

	    if (!usuario.verificarIdade(usuario.calcularIdade())) {
	        throw new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "O usuário deve ter pelo menos 18 anos!"
	        );
	    }

	    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
	    usuario.setId(null);

	    return Optional.of(usuarioRepository.save(usuario));
	
	}
// Método para atualizar um usuário existente. Verifica se o usuário existe e se o e-mail já está em uso antes de salvar as alterações.
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (usuarioRepository.findById(usuario.getId()).isEmpty()) {
			return Optional.empty();
		}

		Optional<Usuario> usuarioExistente = usuarioRepository.findByUsuario(usuario.getUsuario());
		
		if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O e-mail já está em uso!", null);
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return Optional.ofNullable(usuarioRepository.save(usuario));
	}
	// Método para autenticar um usuário. Verifica se as credenciais são válidas e retorna um token JWT se a autenticação for bem-sucedida.
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		if (usuarioLogin.isEmpty()) {
			return Optional.empty();
		}

		UsuarioLogin login = usuarioLogin.get();
		
		try {
 
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(login.getUsuario(), login.getSenha()));

			return usuarioRepository.findByUsuario(login.getUsuario())
				.map(usuario -> construirRespostaLogin(login, usuario));

		} catch (Exception e) {

			return Optional.empty();
		}
	}
// Método auxiliar para construir a resposta de login, preenchendo os campos necessários e gerando um token JWT.
	private UsuarioLogin construirRespostaLogin(UsuarioLogin usuarioLogin, Usuario usuario) {
		
		usuarioLogin.setId(usuario.getId());
		usuarioLogin.setNome(usuario.getNome());
		usuarioLogin.setFoto(usuario.getFoto());
		usuarioLogin.setSenha("");
		usuarioLogin.setToken(gerarToken(usuario.getUsuario()));
		return usuarioLogin;
		
	}

	private String gerarToken(String usuario) {
		// Obs tem que ter espaco depois do bearer senao o token vem quebrado
		return "Bearer " + jwtService.generateToken(usuario);
	}
}