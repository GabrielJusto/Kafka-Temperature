package br.com.bonatto.repository.client;

import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;
import br.com.bonatto.repository.Point.PointRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepository {

    private Connection con;

    public ClientRepository(Connection con) {
        this.con = con;
    }


    private int getPointId(Client client)
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
        int pointId = getPointId(client);

        String sql = "UPDATE client SET pointId = ?, connector = ?, maxPrice = ?, walletKey = ?, timeToCharge = ?, charging = ? WHERE clientId = ? ";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i = 1;
            pstmt.setInt(i++, pointId);
            pstmt.setString(i++, client.getConnector());
            pstmt.setDouble(i++, client.getMaxPrice());
            pstmt.setString(i++, client.getWalletKey());
            pstmt.setLong(i++, client.getTimeToCharge());
            pstmt.setBoolean(i++, client.isCharging());
            pstmt.setLong(i++, client.getId());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insert (Client client)
    {

        int pointId = getPointId(client);

        String sql = "INSERT INTO client ( clientId, pointId, connector, maxPrice, walletKey, timeToCharge. charging) VALUES (?,?,?,?,?,?,?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i=1;
            pstmt.setLong(i++, client.getId());
            pstmt.setInt(i++, pointId);
            pstmt.setString(i++, client.getConnector());
            pstmt.setDouble(i++, client.getMaxPrice());
            pstmt.setString(i++, client.getWalletKey());
            pstmt.setLong(i++, client.getTimeToCharge());
            pstmt.setBoolean(i++, client.isCharging());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Client getById(long id)
    {
        String sql = "SELECT clientId, pointId, connector, maxPrice, walletKey, timeToCharge. charging FROM client WHERE clientId = ? ";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            pstmt.setLong(1, id);

            try(ResultSet rs = pstmt.executeQuery())
            {
                PointRepository pointRepository = new PointRepository(con);


                if (rs.next()) {
                    Point p = pointRepository.select(rs.getInt("pointId"));
                    return new Client(
                            rs.getLong("clientId"),
                            p,
                            rs.getString("connector"),
                            rs.getDouble("maxPrice"),
                            rs.getString("walletKey"),
                            rs.getLong("timeToCharge"),
                            rs.getBoolean("charging")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
