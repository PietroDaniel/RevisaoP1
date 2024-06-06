package model.repository.vacinacao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.entity.Vacina;
import model.entity.Vacinacao;
import model.repository.Banco;
import model.repository.BaseRepository;

public class VacinacaoRepository implements BaseRepository<Vacinacao> {

	@Override
	public Vacinacao salvar(Vacinacao novaAplicacaoVacina) {
		String sql = " INSERT INTO P1.aplicacao_vacina "
				+ " (id_pessoa, id_vacina, data_aplicacao, avaliacao) "
				+ " VALUES(?, ?, ?, ?) ";
		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatementWithPk(conexao, sql);
		
		try {
			preencherValoresSqlInsertUpdate(stmt, novaAplicacaoVacina);
			
			stmt.execute();
			ResultSet resultado = stmt.getGeneratedKeys();
			if(resultado.next()) {
				novaAplicacaoVacina.setId(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao salvar nova vacinação");
			System.out.println("Erro: " + e.getMessage());
		}finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conexao);
		}
		
		return novaAplicacaoVacina;
	}

	private void preencherValoresSqlInsertUpdate(PreparedStatement stmt, Vacinacao aplicacaoVacina) 
			throws SQLException {
		stmt.setInt(1, aplicacaoVacina.getIdPessoa());
		stmt.setInt(2, aplicacaoVacina.getVacina().getId());
		stmt.setDate(3, Date.valueOf(aplicacaoVacina.getDataAplicacao()));
		stmt.setInt(4, aplicacaoVacina.getAvaliacao());
	}

	@Override
	public boolean excluir(int id) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean excluiu = false;
		String query = "DELETE FROM aplicacao_vacina WHERE id = " + id;
		try {
			excluiu = (stmt.executeUpdate(query) == 1);
		} catch (SQLException erro) {
			System.out.println("Erro ao excluir vacinação");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return excluiu;
	}

	@Override
	public boolean alterar(Vacinacao aplicacaoVacinaAlterada) {
		boolean alterou = false;
		String query = " UPDATE aplicacao_vacina "
				     + " SET id_pessoa=?, id_vacina=?, data_aplicacao=?, avaliacao=? "
				     + " WHERE id=? ";
		Connection conn = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			preencherValoresSqlInsertUpdate(stmt, aplicacaoVacinaAlterada);
			
			stmt.setInt(5, aplicacaoVacinaAlterada.getId());
			alterou = stmt.executeUpdate() > 0;
		} catch (SQLException erro) {
			System.out.println("Erro ao atualizar vacina");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return alterou;
	}

	@Override
	public Vacinacao consultarPorId(int id) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		
		Vacinacao aplicacaoVacina = null;
		ResultSet resultado = null;
		String query = " SELECT * FROM aplicacao_vacina WHERE id = " + id;
		try{
			resultado = stmt.executeQuery(query);

			if(resultado.next()){
				aplicacaoVacina = converterParaObjeto(resultado);
			}
		} catch (SQLException erro){
			System.out.println("Erro ao consultar vacinação com o id: " + id);
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return aplicacaoVacina;
	}

	private Vacinacao converterParaObjeto(ResultSet resultado) throws SQLException {
		Vacinacao aplicacaoVacina = new Vacinacao();
		aplicacaoVacina.setId(resultado.getInt("ID"));
		aplicacaoVacina.setIdPessoa(resultado.getInt("ID_PESSOA"));
		aplicacaoVacina.setAvaliacao(resultado.getInt("AVALIACAO"));
		aplicacaoVacina.setDataAplicacao(resultado.getDate("DATA_APLICACAO").toLocalDate());
		
		VacinaRepository vacinaRepository = new VacinaRepository();
		Vacina vacinaAplicada = 
				vacinaRepository.consultarPorId(resultado.getInt("ID_VACINA"));
		
		aplicacaoVacina.setVacina(vacinaAplicada);
		return aplicacaoVacina;
	}

	@Override
	public ArrayList<Vacinacao> consultarTodos() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		
		ArrayList<Vacinacao> aplicacoes = new ArrayList<Vacinacao>();
		ResultSet resultado = null;
		String query = " SELECT * FROM aplicacao_vacina";
		try{
			resultado = stmt.executeQuery(query);

			while(resultado.next()){
				Vacinacao aplicacaoVacina = this.converterParaObjeto(resultado);
				aplicacoes.add(aplicacaoVacina);
			}
		} catch (SQLException erro){
			System.out.println("Erro ao consultar todas as vacinações realizadas");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return aplicacoes;
	}
	
	public ArrayList<Vacinacao> consultarPorIdPessoa(int idPessoa){
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		
		ArrayList<Vacinacao> aplicacoes = new ArrayList<Vacinacao>();
		ResultSet resultado = null;
		String query = " SELECT * FROM aplicacao_vacina WHERE id_pessoa = " + idPessoa;
		try{
			resultado = stmt.executeQuery(query);

			while(resultado.next()){
				Vacinacao aplicacaoVacina = this.converterParaObjeto(resultado);
				aplicacoes.add(aplicacaoVacina);
			}
		} catch (SQLException erro){
			System.out.println("Erro ao consultar todas as vacinações realizadas na pessoa com id" + idPessoa);
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return aplicacoes;
	}
	
	public void atualizarMediaVacina(int idVacina) {
	    String sql = "UPDATE vacina SET media = (SELECT AVG(avaliacao) FROM aplicacao_vacina WHERE id_vacina = ?) WHERE id = ?";
	    Connection conexao = Banco.getConnection();
	    PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);

	    try {
	        stmt.setInt(1, idVacina);
	        stmt.setInt(2, idVacina);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        Banco.closeStatement(stmt);
	        Banco.closeConnection(conexao);
	    }
	}


}
