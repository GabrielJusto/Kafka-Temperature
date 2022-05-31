package br.com.bonatto.repository.client;

import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;
import br.com.bonatto.repository.Point.PointRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientRepository {

    private Connection con;

    public ClientRepository(Connection con) {
        this.con = con;
    }


    private int getPoitId(Client client)
    {
        PointRepository pointRepository = new PointRepository(con);
        Point p = pointRepository.select(client.getLocal().getLat(), client.getLocal().getLon());

        if(p == null)
            return pointRepository.insert(client.getLocal());
        else
            return p.getId();
    }


    public void update(Client client)
    {
        int pointId = getPoitId(client);

        String sql = "UPDATE client SET pointId = ?, connector = ?, maxPrice = ?, walletKey = ? WHERE clientId = ? ";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i = 1;
            pstmt.setInt(i++, pointId);
            pstmt.setString(i++, client.getConnector());
            pstmt.setDouble(i++, client.getMaxPrice());
            pstmt.setString(i++, client.getWalletKey());
            pstmt.setInt(i++, client.getId());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insert (Client client)
    {

        int pointId = getPoitId(client);

        String sql = "INSERT INTO client ( clientId, pointId, connector, maxPrice, walletKey) VALUES (?,?,?,?,?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i=1;
            pstmt.setInt(i++, client.getId());
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
