import java.sql.*;
import java.util.HashSet;

public class Examples {
    public static void main(String[] args) {
        createTable();
//        Course course = new Course("Java",30,3000);
//        insertIntoCourse(course);
//        Course course1 = new Course("PHP",20,5000);
//        insertIntoCourse(course1);


//
//        System.out.println("Курсы где цена выше 4000" + getByPrice(4000));

        deleteById(52);
        System.out.println(selectAll());
    }

    //строка подключения
    private static final String CONNECTION_STR = "jdbc:postgresql://localhost:5432/testik";
    // логин
    private static final String LOGIN = "postgres";
    // пароль
    private static final String PASSWORD = "Arrow1997";


    private static void createTable() {
        String createString = "CREATE TABLE IF NOT EXISTS course (" +
                "id SERIAL PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "duration SMALLINT," +
                "price NUMERIC(9,2)" +
                ")";
        try {
            Class.forName("org.postgresql.Driver"); //загрузка драйвера
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден");
        }
        //подключение
        try (Connection connection = DriverManager.getConnection(CONNECTION_STR, LOGIN, PASSWORD);){
            try (Statement statement = connection.createStatement()) {
                System.out.println(statement.executeUpdate(createString));
            }
        } catch (SQLException throwables) {
            System.out.println("Не удалось выполнить запрос" + throwables.getMessage());
        }

    }
    public static void insertIntoCourse(Course course){
        String insertSql = "INSERT INTO course(title,duration,price)" + "VALUES(?,?,?)";

        try {   // регистрация драйвера, загрузка класса
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден");
        }

        try(Connection connection = DriverManager.getConnection(CONNECTION_STR,LOGIN,PASSWORD)) {
            try( PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setString(1, course.getTitle());
                statement.setInt(2,course.getDuration());
                statement.setDouble(3,course.getPrice());
                System.out.println(statement.executeUpdate());
            }
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос" + e.getMessage());
        }
    }

    public static HashSet<Course> selectAll(){
        String selectAll = "SELECT * FROM course";
        HashSet<Course> courses = new HashSet<>();


        try {   // регистрация драйвера, загрузка класса
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден");
        }


        try( Connection connection = DriverManager.getConnection(CONNECTION_STR,LOGIN,PASSWORD)){
            try(Statement statement = connection.createStatement()){
                try(ResultSet result = statement.executeQuery(selectAll)){

                    while (result.next()){
                        int id = result.getInt("id");
                        String title = result.getString("title");
                        int duration = result.getInt("duration");
                        double price = result.getDouble("price");
                        Course course = new Course(title, duration, price);
                        course.setId(id);
                        courses.add(course);
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос" + e.getMessage());
        }
        return courses;
    }

    public static HashSet<Course> getByPrice(double price){
        try {   // регистрация драйвера, загрузка класса
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден");
        }
        String selectByPrice = "SELECT * FROM course WHERE price > ?";
        HashSet<Course> courses = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(CONNECTION_STR,LOGIN,PASSWORD)) {
            try(PreparedStatement statement = connection.prepareStatement(selectByPrice)){
                statement.setDouble(1,price);
                try(ResultSet result = statement.executeQuery()){
                    while (result.next()){
                        int id = result.getInt("id");
                        String title = result.getString("title");
                        int duration = result.getInt("duration");
                        double resPrice = result.getDouble("price");
                        Course course = new Course(title, duration, resPrice);
                        course.setId(id);
                        courses.add(course);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос " + e.getMessage());
        }
        return courses;
    }
    public static void deleteById(int id){
        String deleteSql  = "DELETE FROM course WHERE id = ?";

        try {   // регистрация драйвера, загрузка класса
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден");
        }

        try{
            Connection connection = DriverManager.getConnection(CONNECTION_STR,LOGIN,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setInt(1,id);
            statement.executeUpdate();

            System.out.println("Record deleted successfully");


            connection.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос " + e.getMessage());
        }


    }
}
