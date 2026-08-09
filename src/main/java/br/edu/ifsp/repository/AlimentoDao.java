package br.edu.ifsp.repository;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.enums.UnidadeMedida;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlimentoDao {

    public void inserir(Alimento alimento) {
        String sqlQuery = """
                    INSERT INTO alimento (nome, proteina,  gordura,  carboidrato, calorias, unidade_medida)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            InformacaoNutricional info = alimento.getInfoNutricional();

            ps.setString(1, alimento.getNome());
            ps.setDouble(2, info.getProteina());
            ps.setDouble(3, info.getGordura());
            ps.setDouble(4, info.getCarboidrato());
            ps.setDouble(5, info.getCalorias());
            ps.setString(6, alimento.getUnidadeMedida().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    alimento.setId(rs.getLong(1));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao cadastrar alimento no banco. ", e);
        }
    }

    public Optional<Alimento> buscarPorId(Long id) {
        String sqlQuery ="""
                SELECT id_alimento, nome, proteina, gordura, carboidrato, calorias, unidade_medida
				FROM alimento
				WHERE id_alimento = ?
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapearAlimento(rs));
                }
            }
            return Optional.empty();
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao buscar alimento com id: " + id, e);
        }

    }


    public  List<Alimento> listarTodosAlimentos() {
        String sqlQuery = """
                SELECT  id_alimento, nome, proteina, gordura, carboidrato, calorias, unidade_medida
			    FROM alimento
                 """;

        List<Alimento> cadastrados = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()){
                cadastrados.add(mapearAlimento(rs));
            }

            return Collections.unmodifiableList(cadastrados);
        }catch (SQLException e){
            throw new RuntimeException("Erro ao listar alimentos cadastrados. ", e);
        }

    }

    public int alterarAlimento(Alimento alimento) {
        String sqlQuery = """
                UPDATE alimento
				SET nome = ?, proteina = ?, gordura = ?, carboidrato = ?, calorias = ?, unidade_medida = ?
				WHERE id_alimento = ?
				""";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            InformacaoNutricional info = alimento.getInfoNutricional();

            ps.setString(1, alimento.getNome());
            ps.setDouble(2, info.getProteina());
            ps.setDouble(3, info.getGordura());
            ps.setDouble(4, info.getCarboidrato());
            ps.setDouble(5, info.getCalorias());
            ps.setString(6, alimento.getUnidadeMedida().name());

            ps.setLong(7, alimento.getId());
            return ps.executeUpdate();
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao alterar cadastro de alimento. ", e);
        }
    }

    public int removerAlimento(Long id) {
        String sqlQuery = "DELETE FROM alimento WHERE id_alimento = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover alimento. ", e);
        }
    }

    private Alimento mapearAlimento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_alimento");
        String nome =  rs.getString("nome");

        InformacaoNutricional info = new InformacaoNutricional(
                rs.getDouble("calorias"),
                rs.getDouble("proteina"),
                rs.getDouble("carboidrato"),
                rs.getDouble("gordura")
        );

        UnidadeMedida unidadeMedida = UnidadeMedida.valueOf(rs.getString("unidade_medida"));

        return new Alimento(id, nome, info, unidadeMedida);
    }
}
