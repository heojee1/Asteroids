package aoop.asteroids.database;

import java.sql.*;

/**
 * Database has multiple static methods that allows the program
 * to create, insert, and read from the database (namely all_scores)
 */
public class Database {
    /** url to the database (it creates a new data when one doesn't exist) **/
    private final static String URL = "jdbc:mysql://localhost:3306/scores?createDatabaseIfNotExist=true";
    /** username **/
    private final static String USERNMAE = "java";
    /** password **/
    private final static String PASSWORD = "Java1234";
    /** SQL query that selects top ten scores from the table **/
    private final static String TOP_TEN_QUERY = "SELECT * FROM scores.all_scores ORDER BY score DESC LIMIT 10;";

    /**
     * connect to the database
     * @return connection
     */
    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNMAE, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * inserts a new row to the database
     *
     * @param nickname the nickname of the player
     * @param score the score of the player
     */
    public static void insert(String nickname, int score) {
        String sql = "INSERT INTO all_scores(user_id, score) VALUES(?,?)";
        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieves top ten scores from the table and returns in form of String
     * @return top ten scoring players and scores in String format
     */
    public static String getTopTenScorers() {
        StringBuilder sb = new StringBuilder();
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(TOP_TEN_QUERY);
            while(rset.next()) {
                String str = String.format(" %1$-10s : %2$-5d\n", rset.getString("user_id"), rset.getInt("score"));
                sb.append(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
