package service;

import java.util.List;
import exception.VacinacaoException;
import model.entity.Pessoa;
import model.entity.Vacina;
import model.entity.Vacinacao;
import model.repository.vacinacao.PessoaRepository;
import model.repository.vacinacao.VacinaRepository;
import model.repository.vacinacao.VacinacaoRepository;

public class VacinacaoService {

	private static final int NOTA_MAXIMA = 5;
	private VacinacaoRepository vacinacaoRepository = new VacinacaoRepository();
	private PessoaRepository pessoaRepository = new PessoaRepository();
	private VacinaRepository vacinaRepository = new VacinaRepository();

	public Vacinacao salvar(Vacinacao novaVacinacao) throws VacinacaoException {
		validarVacinacao(novaVacinacao);
		novaVacinacao = vacinacaoRepository.salvar(novaVacinacao);
		vacinacaoRepository.atualizarMediaVacina(novaVacinacao.getVacina().getId());
		return novaVacinacao;
	}

	public boolean atualizar(Vacinacao vacinacaoEditada) throws VacinacaoException {
		validarVacinacao(vacinacaoEditada);
		boolean atualizado = vacinacaoRepository.alterar(vacinacaoEditada);
		if (atualizado) {
			vacinacaoRepository.atualizarMediaVacina(vacinacaoEditada.getVacina().getId());
		}
		return atualizado;
	}

	public boolean excluir(int id) {
		return vacinacaoRepository.excluir(id);
	}

	public Vacinacao consultarPorId(int id) {
		return vacinacaoRepository.consultarPorId(id);
	}

	public List<Vacinacao> consultarTodas() {
		return vacinacaoRepository.consultarTodos();
	}

	public List<Vacinacao> consultarPorIdPessoa(int idPessoa) {
		return vacinacaoRepository.consultarPorIdPessoa(idPessoa);
	}

	private void validarVacinacao(Vacinacao vacinacao) throws VacinacaoException {
		Pessoa pessoa = pessoaRepository.consultarPorId(vacinacao.getIdPessoa());
		Vacina vacina = vacinaRepository.consultarPorId(vacinacao.getVacina().getId());

		if (pessoa == null) {
			throw new VacinacaoException("Pessoa com ID " + vacinacao.getIdPessoa() + " não encontrada.");
		}
		if (vacina == null) {
			throw new VacinacaoException("Vacina com ID " + vacinacao.getVacina().getId() + " não encontrada.");
		}

		System.out.println("Pessoa: " + pessoa.getNome());
		System.out.println("Vacina: " + vacina.getNome());

		if (vacina.getEstagio() == 1 && pessoa.getTipo() != 1) {
			throw new VacinacaoException("Somente pesquisadores podem receber vacinas em estágio inicial.");
		}
		if (vacina.getEstagio() == 2 && pessoa.getTipo() == 3) {
			throw new VacinacaoException(
					"Somente pesquisadores e voluntários podem receber vacinas em estágio de testes.");
		}

	}

}
