package br.senai.sp.cfp138.restaguide.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp138.restaguide.model.Restaurante;
import br.senai.sp.cfp138.restaguide.repository.RestauranteRepository;

//caso queira paginação, criamos um método aqui para vir com paginação
@RequestMapping("api/restaurante")
@RestController
public class RestauranteRestController {
	@Autowired
	private RestauranteRepository repository;
	

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Restaurante> getRestaurante() {
		return repository.findAll();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<Restaurante> findRestaurante (@PathVariable("id") Long idRestaurante){
			//busca o restaurante
			Optional<Restaurante> restaurante = repository.findById(idRestaurante);
			if (restaurante.isPresent()) {
				return ResponseEntity.ok(restaurante.get());
			}else {
				return ResponseEntity.notFound().build();
			}
		}
	@RequestMapping(value = "/tipo/{idTipo}", method = RequestMethod.GET)
	public Iterable<Restaurante> getRestauranteByTipo (@PathVariable("idTipo") Long idTipo){
		return repository.findByTipoRestauranteId(idTipo);
		
	}
	@RequestMapping(value = "/espaco/{espacoInfantil}", method = RequestMethod.GET)
	public Iterable<Restaurante> getRestauranteByTipo (@PathVariable("espacoInfantil") boolean espacoInfantil){
		return repository.findByEspacoInfantil(espacoInfantil);
	}
	
}

