package br.com.bonatto.repository.Point;

import br.com.bonatto.model.Point;

import java.sql.*;

public class PointRepository
{
    private Connection con;

    public PointRepository(Connection con) {
        this.con = con;
    }

    public int insert(Point point)
    {
        String sql = "INSERT INTO point (lat, lon) VALUES (?,?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            int i =1;
            pstmt.setDouble(i++, point.getLat());
            pstmt.setDouble(i++, point.getLon());

            pstmt.execute();
            try(ResultSet rs = pstmt.getGeneratedKeys())
            {
                if(rs.next())
                    return rs.getInt(1);
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
