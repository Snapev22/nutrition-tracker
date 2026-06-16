package br.edu.ifsp.repository;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.PlanoDiario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlanoDiarioDao {

    public void inserirPlanoDiario(PlanoDiario plano) {
        String sqlQuery = """
                INSERT INTO plano_diario (idaluno, data)
                VALUES (?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, plano.getAluno().getId());
            ps.setDate(2, Date.valueOf(plano.getData()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    plano.setId(rs.getLong(1));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao cadastrar plano no banco. ", e);
        }
    }

    public List<PlanoDiario> buscarPorAluno(Long idAluno){
        String sqlQuery = """
               SELECT id, idaluno, data
               FROM plano_diario
               WHERE idaluno = ?
               """;

        List<PlanoDiario> planos = new ArrayList<>();

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlQuery)){

            ps.setLong(1, idAluno);
            try(ResultSet rs = ps.executeQuery()){
                AlunoDao alunoDao = new AlunoDao();

                while(rs.next()){
                    Long idPlano = rs.getLong("id");
                    LocalDate data = rs.getDate("data").toLocalDate();

                    PlanoDiario plano = new PlanoDiario();

                    plano.setId(idPlano);
                    plano.setData(data);
                    plano.setAluno(alunoDao.
                            buscarPorId(idAluno)
                            .orElseThrow(() -> new RuntimeException("Aluno não encontrado. Id: " + idAluno))
                            );

                    planos.add(plano);
                }
            }
            return planos;
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar por aluno vinculado a plano no banco. ", e);
        }
    }

    public Optional<PlanoDiario> buscarPorAlunoEData(Long idAluno, LocalDate dataBuscada){
        String sqlQuery ="""
                SELECT  id, idaluno, data
				FROM plano_diario
				WHERE idaluno = ? AND data = ?
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, idAluno);
            ps.setDate(2, Date.valueOf(dataBuscada));

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    AlunoDao alunoDao = new AlunoDao();
                    Long idPlano = rs.getLong("id");
                    LocalDate data = rs.getDate("data").toLocalDate();

                    PlanoDiario plano = new PlanoDiario();
                    plano.setId(idPlano);
                    plano.setData(data);
                    plano.setAluno(alunoDao.buscarPorId(idAluno)
                            .orElseThrow(() -> new RuntimeException("Aluno não encontrado. Id: " + idAluno)));

                    return Optional.of(plano);
                }
            }
            return Optional.empty();
        }catch (SQLException e) {
            throw new RuntimeException("Erro buscar plano vinculado a aluno e data. IdAluno: " + idAluno +
                    " Data: " + dataBuscada, e);
        }
    }

    public int removerPlanoDiario(Long id) {
        String sqlQuery = "DELETE FROM plano_diario WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover alimento. ", e);
        }
    }
}
