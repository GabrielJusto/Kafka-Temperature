package br.com.bonatto.repository.client;

import br.com.bonatto.model.Client;
import br.com.bonatto.repository.Point.PointRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientRepository
{

    private Connection con;

    public ClientRepository(Connection con) {
        this.con = con;
    }

    public void insert (Client client)
    {

        PointRepository pointRepository = new PointRepository(con);
        int pointId = pointRepository.insert(client.getLocal());

        String sql = "INSERT INTO client ( pointId, connector, maxPrice, walletKey) VALUES (?,?,?,?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i=1;
            pstmt.setInt(i++, pointId);
            pstmt.setString(i++, client.getConnector());
            pstmt.setDouble(i++, client.getMaxPrice());
            pstmt.setString(i++, client.getWalletKey());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
