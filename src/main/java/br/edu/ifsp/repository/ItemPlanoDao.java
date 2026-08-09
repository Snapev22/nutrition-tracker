package br.edu.ifsp.repository;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.ItemPlano;
import br.edu.ifsp.model.ResumoNutricional;
import br.edu.ifsp.model.enums.UnidadeMedida;

import java.sql.*;
import java.util.ArrayList;
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
            ps.setDouble(2, itemPlano.getTotalNutricional().getCalorias());
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
                a.nome, a.proteina, a.carboidrato, a.gordura, a.calorias, a.unidade_medida
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

                    InformacaoNutricional info = new InformacaoNutricional(
                            rs.getDouble("calorias"),
                            rs.getDouble("proteina"),
                            rs.getDouble("carboidrato"),
                            rs.getDouble("gordura")
                    );

                    UnidadeMedida unidadeMedida = UnidadeMedida.valueOf(rs.getString("unidade_medida"));

                    Alimento alimento = new Alimento(
                            rs.getLong("id_alimento"),
                            rs.getString("nome"),
                            info,
                            unidadeMedida
                    );

                    ItemPlano itemPlano = new ItemPlano();

                    itemPlano.setId(rs.getLong("id_item_plano"));
                    itemPlano.setAlimento(alimento);
                    itemPlano.setQuantidade(rs.getDouble("quantidade"));
                    itemPlano.calcularTotalNutricional();

                    itens.add(itemPlano);
                }
            }
            return itens;
        }catch (SQLException e){
            throw  new RuntimeException("Erro ao buscar por plano relacionado a item. ", e);
        }
    }

    public InformacaoNutricional somarResumoPorPlano(Long planoId) {
        String sqlQuery = """
            SELECT
                COALESCE(SUM(ip.calorias_totais), 0) AS total_calorias,
                COALESCE(SUM(
                    CASE WHEN a.unidade_medida = 'UNIDADE'
                         THEN ip.quantidade * a.proteina
                         ELSE (ip.quantidade / 100.0) * a.proteina
                    END
                ), 0) AS total_proteina,
                COALESCE(SUM(
                    CASE WHEN a.unidade_medida = 'UNIDADE'
                         THEN ip.quantidade * a.carboidrato
                         ELSE (ip.quantidade / 100.0) * a.carboidrato
                    END
                ), 0) AS total_carboidrato,
                COALESCE(SUM(
                    CASE WHEN a.unidade_medida = 'UNIDADE'
                         THEN ip.quantidade * a.gordura
                         ELSE (ip.quantidade / 100.0) * a.gordura
                    END
                ), 0) AS total_gordura
            FROM item_plano ip
            INNER JOIN alimento a ON ip.id_alimento = a.id_alimento
            WHERE ip.id_plano_diario = ?
            """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setLong(1, planoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InformacaoNutricional(
                            rs.getDouble("total_calorias"),
                            rs.getDouble("total_proteina"),
                            rs.getDouble("total_carboidrato"),
                            rs.getDouble("total_gordura")
                    );
                }
            }
            return InformacaoNutricional.zero();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao somar resumo nutricional do plano. ", e);
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
