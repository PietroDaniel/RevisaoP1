package service;

import java.util.List;

import exception.VacinacaoException;
import model.entity.Vacina;
import model.repository.vacinacao.VacinaRepository;

public class VacinaService {

	private VacinaRepository repository = new VacinaRepository();
	
	public Vacina salvar(Vacina novaVacina){
		return repository.salvar(novaVacina);
	}

	public boolean atualizar(Vacina vacinaEditada) {
		return repository.alterar(vacinaEditada);
	}

	public boolean excluir(int id) throws VacinacaoException {
	    if (!repository.podeExcluirVacina(id)) {
	        throw new VacinacaoException("Vacina não pode ser excluída pois já foi aplicada.");
	    }
	    return repository.excluir(id);
	}

	public Vacina consultarPorId(int id) {
		return repository.consultarPorId(id);
	}

	public List<Vacina> consultarTodas() {
		return repository.consultarTodos();
	}
}
