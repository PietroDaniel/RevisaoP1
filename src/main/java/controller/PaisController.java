package controller;

import exception.VacinacaoException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.entity.Pais;
import service.PaisService;

@Path("/pais")
public class PaisController {
	
	private PaisService service = new PaisService();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pais salvar(Pais novo) throws VacinacaoException{
		 return service.salvar(novo);
	}
	
	@GET
	@Path("/{id}")
	public Pais consultarPorId(@PathParam("id") int id){
		 return service.consultarPorId(id);
	}
}