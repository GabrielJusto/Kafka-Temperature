package br.com.bonatto.repository.station;

import br.com.bonatto.model.Point;
import br.com.bonatto.model.Station;
import br.com.bonatto.model.StationInfo;
import br.com.bonatto.repository.Point.PointRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StationRepository
{
    private Connection con;

    public StationRepository(Connection con) {
        this.con = con;
    }


    public void update(StationInfo station)
    {
        String sql = "UPDATE station SET busy = ?, price = ?, busyTime = ?, timeToCharge = ?, temperature = ? WHERE stationId = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i = 1;
            pstmt.setBoolean(i++, station.isBusy());
            pstmt.setDouble(i++, station.getPrice());
            pstmt.setLong(i++, station.getBusyTime());
            pstmt.setLong(i++, station.getTimeToCharge());
            pstmt.setDouble(i++, station.getTemperature());
            pstmt.setLong(i++, station.getId());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Station station)
    {

        PointRepository pointRepository = new PointRepository(con);
        Point p = pointRepository.select(station.getLocal().getLat(), station.getLocal().getLon());
        int pointId;
        if(p == null)
            pointId = pointRepository.insert(station.getLocal());
        else
            pointId = p.getId();

        String sql = "INSERT INTO station(stationId, pointId, connector, fastCharge, brand) VALUES (?,?,?,?,?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql))
        {
            int i = 1;
            pstmt.setLong(i++, station.getId());
            pstmt.setInt(i++, pointId);
            pstmt.setString(i++, station.getConnector());
            pstmt.setBoolean(i++, station.isFastCharge());
            pstmt.setString(i++, station.getBrand());

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
