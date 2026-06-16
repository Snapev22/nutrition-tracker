package br.edu.ifsp.repository;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlunoDao {

    public void inserir(Aluno aluno)  {
        String sqlQuery = """
                INSERT INTO aluno (nome, idade, peso, altura, sexo, fator_atividade, objetivo, meta_calorica)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, aluno.getNome());
            ps.setInt(2, aluno.getIdade());
            ps.setDouble(3, aluno.getPeso());
            ps.setDouble(4, aluno.getAltura());
            ps.setString(5, aluno.getSexo().name());
            ps.setString(6, aluno.getFatorAtividade().name());
            ps.setString(7, aluno.getObjetivo().name());
            ps.setDouble(8, aluno.getMetaCalorias());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getLong(1));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao cadastrar aluno. ", e);
        }
    }

    public Optional<Aluno> buscarPorId(Long id) {
        String sqlQuery ="""
                SELECT idaluno, nome, idade, peso, altura, sexo, fator_atividade, objetivo,
                meta_calorica
				FROM aluno
				WHERE idaluno = ?
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                   return Optional.of(mapearAluno(rs));
                }
            }
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao buscar aluno com id: " + id, e);
        }
        return Optional.empty();
     }

     public List<Aluno> listarTodos() {
         String sqlQuery = """
                SELECT idaluno, nome, idade, peso, altura, sexo, fator_atividade, objetivo,
                meta_calorica
				 FROM aluno
                 """;

         List<Aluno> cadastrados = new ArrayList<>();

         try (Connection connection = ConnectionFactory.getConnection();
              PreparedStatement ps = connection.prepareStatement(sqlQuery);
                ResultSet rs = ps.executeQuery()) {

             while (rs.next()){
                 cadastrados.add(mapearAluno(rs));
             }

     }catch (SQLException e){
             throw new RuntimeException("Erro ao listar alunos cadastrados. ", e);
         }
         return Collections.unmodifiableList(cadastrados);
    }


    public int alterar(Aluno aluno)  {
        String sqlQuery = """
                UPDATE aluno
				SET nome = ?, idade = ?, peso = ?, altura = ?, sexo = ?, fator_atividade = ?, 
				    objetivo = ?, meta_calorica = ?
				WHERE idaluno = ?
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, aluno.getNome());
            ps.setInt(2, aluno.getIdade());
            ps.setDouble(3, aluno.getPeso());
            ps.setDouble(4, aluno.getAltura());
            ps.setString(5, aluno.getSexo().name());
            ps.setString(6, aluno.getFatorAtividade().name());
            ps.setString(7, aluno.getObjetivo().name());
            ps.setDouble(8, aluno.getMetaCalorias());

            ps.setLong(9, aluno.getId());
            return ps.executeUpdate();
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao alterar cadastro de aluno. ", e);
        }
    }

    public int remover(Long id) {
        String sqlQuery = "DELETE FROM aluno WHERE idaluno = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover aluno. ", e);
        }
    }

     private Aluno mapearAluno(ResultSet rs) throws SQLException {
         Long id = rs.getLong("idaluno");
         String nome =  rs.getString("nome");
         int idade = rs.getInt("idade");
         double peso = rs.getDouble("peso");
         double altura = rs.getDouble("altura");
         String sexo = rs.getString("sexo");
         String fator_atividade = rs.getString("fator_atividade");
         String objetivo = rs.getString("objetivo");
         double meta_calorica = rs.getDouble("meta_calorica");

        return  new Aluno(
                id, nome, idade, peso, altura,
                Sexo.valueOf(sexo), FatorAtividade.valueOf(fator_atividade),
                Objetivo.valueOf(objetivo),
                meta_calorica );
     }
}
