package br.senai.sp.cfp138.restaguide.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp138.restaguide.annotation.Privado;
import br.senai.sp.cfp138.restaguide.annotation.Publico;
import br.senai.sp.cfp138.restaguide.model.Erro;
import br.senai.sp.cfp138.restaguide.model.Usuario;
import br.senai.sp.cfp138.restaguide.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {
	@Autowired
	private UsuarioRepository repository;

//se você vai manupular a resposta coloca responseentity, se não response
	@Publico
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	//no corpo da requisição virá um objeto usuario
	private ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario) {

		try {
				//salvar o usiario 
				repository.save(usuario);
				//retornar 2021, retorna location, devolver objeto que foi gerado
				//entidade de resposta, aceita uma URI
				
				//retorna codigo 2021, com a URL para acesso no Location e o usuario inserido no corpo da resposta
				
				
				return ResponseEntity.created(URI.create("/api/usuario/"+usuario.getId())).body(usuario);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro de Constraint: Registro duplicado");
			erro.setException(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}catch (Exception e) {
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro de Constraint: Registro duplicado"+e.getMessage());
			erro.setException(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}
	//pegar algo que tem no corpo e no caminho
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizaUsuario(@RequestBody Usuario usuario, @PathVariable("id")Long id){
		//valida o ID
		if(id !=usuario.getId()) {
			throw new RuntimeException("ID Iválido");
		}
		//salva o usuario no banco
		repository.save(usuario);
		//manipular cabecalho de uma forma mais manual
		//criar um cabecalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/usuario/"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}
	@RequestMapping(value = "/restaurante/{idRest}", method = RequestMethod.GET)
	public List<Usuario> listarPorRestaurante(@PathVariable("idRest") Long idRest){
		return repository.findbyRestauranteId(idRest);
	}
	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsuario(@PathVariable("id") Long idUsuario){
		repository.deleteById(idUsuario);
		return ResponseEntity.noContent().build();
	}
}
