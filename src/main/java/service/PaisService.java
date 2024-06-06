package service;

import exception.VacinacaoException;
import model.entity.Pais;
import model.repository.vacinacao.PaisRepository;

public class PaisService {

	private PaisRepository repository = new PaisRepository();
	
	public Pais salvar(Pais novo) throws VacinacaoException {
		return repository.salvar(novo);
	}
	
	public Pais consultarPorId(int id) {
		return repository.consultarPorId(id);
	}
}
