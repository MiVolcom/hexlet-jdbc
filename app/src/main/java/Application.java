import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

    public static void main(String[] args) throws SQLException {

        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Sarah");
                preparedStatement.setString(2, "33333");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "John");
                preparedStatement.setString(2, "555555");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Will");
                preparedStatement.setString(2, "8888888");
                preparedStatement.executeUpdate();

                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }

            var sqlDelete = "DELETE FROM users WHERE username ='John'";
            try (var statement4 = conn.createStatement()) {
                statement4.execute(sqlDelete);
            }

            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    var a = (resultSet.getLong("id"));
                    var b = (resultSet.getString("username"));
                    var c = (resultSet.getString("phone"));
                    System.out.println(a + " " + b + " " + c);
                }
            }
        }
    }
}