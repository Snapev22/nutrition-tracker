package br.edu.ifsp.repository;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.ItemPlano;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemPlanoDao {

    public void inserirItem(ItemPlano itemPlano, long planoId) {
        String sqlQuery = """
                INSERT INTO item_plano(quantidade, calorias_totais, id_alimento, id_plano_diario)
                VALUES(?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, itemPlano.getQuantidade());
            ps.setDouble(2, itemPlano.getCaloriasTotais());
            ps.setLong(3, itemPlano.getAlimento().getId());
            ps.setLong(4, planoId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    itemPlano.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item no banco. ", e);

        }
    }

    public List<ItemPlano> buscarPorPlano(Long planoId) {
        String sqlQuery = """
                SELECT ip.id_item_plano, ip.quantidade, ip.calorias_totais,
                a.id_alimento,
                a.nome, a.proteina, a.carboidrato, a.gordura, a.calorias
                FROM item_plano ip
                INNER JOIN alimento a
                     ON ip.id_alimento = a.id_alimento
                
                WHERE ip.id_plano_diario = ?
                """;
        List<ItemPlano> itens = new ArrayList<>();
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlQuery)){

            ps.setLong(1, planoId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Alimento alimento = new Alimento();

                    alimento.setId(rs.getLong("id_alimento"));
                    alimento.setNome(rs.getString("nome"));
                    alimento.setProteina(rs.getDouble("proteina"));
                    alimento.setCarboidrato(rs.getDouble("carboidrato"));
                    alimento.setGordura(rs.getDouble("gordura"));
                    alimento.setCalorias(rs.getDouble("calorias"));

                    ItemPlano itemPlano = new ItemPlano();

                    itemPlano.setId(rs.getLong("id_item_plano"));
                    itemPlano.setAlimento(alimento);
                    itemPlano.setQuantidade(rs.getDouble("quantidade"));
                    itemPlano.setCaloriasTotais(rs.getDouble("calorias_totais"));

                    itens.add(itemPlano);
                }
            }
            return itens;
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao buscar por plano relacionado a item. ", e);
        }
    }

    public double somarCaloriasPorPlano(Long planoId){
        String sqlQuery = """
                SELECT COALESCE(SUM(calorias_totais), 0) AS total
                FROM item_plano
                WHERE id_plano_diario = ?
                """;
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlQuery)){

            ps.setLong(1, planoId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getDouble("total");
                }
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao somar calorias por plano. ", e);
        }
    }

    public int removerItem(Long id) {
        String sqlQuery = "DELETE FROM item_plano WHERE id_item_plano = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover item do plano. ", e);
        }
    }
}
