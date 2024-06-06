package model.repository.vacinacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.entity.Pais;
import model.repository.Banco;
import model.repository.BaseRepository;

public class PaisRepository implements BaseRepository<Pais> {

	@Override
	public Pais salvar(Pais novoPais) {
		String sql = " INSERT INTO pais (nome, sigla) "
				   + " VALUES(?, ?) ";
		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatementWithPk(conexao, sql);
		
		try {
			stmt.setString(1, novoPais.getNome());
			stmt.setString(2, novoPais.getSigla());
			
			stmt.execute();
			ResultSet resultado = stmt.getGeneratedKeys();
			if(resultado.next()) {
				novoPais.setId(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao salvar novo país");
			System.out.println("Erro: " + e.getMessage());
		}
		
		return novoPais;
	}

	public Pais consultarPorId(int id) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		
		Pais pais = null;
		ResultSet resultado = null;
		String query = " SELECT * FROM pais WHERE id = " + id;
		
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				pais = new Pais();
				pais.setId(resultado.getInt("ID"));
				pais.setNome(resultado.getString("NOME"));
				pais.setSigla(resultado.getString("SIGLA"));
			}
		} catch (SQLException erro){
			System.out.println("Erro ao consultar país com o id: " + id);
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return pais;
	}

	@Override
	public boolean excluir(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean alterar(Pais entidade) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Pais> consultarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
}
