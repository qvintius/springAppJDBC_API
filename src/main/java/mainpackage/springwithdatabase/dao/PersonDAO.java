package mainpackage.springwithdatabase.dao;

import mainpackage.springwithdatabase.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private static final String URL = "jdbc:postgresql://localhost:5432/firstdbforspring";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";
    //соединение с бд
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {throw new RuntimeException(e);}
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {throw new RuntimeException(e);}
    }
    //закоментирован способ через список
    /*private List<Person> people;
    {
        people = new ArrayList<>();
        people.add(new Person(++PEOPLE_COUNT, "Tom", 24, "tom@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Bob", 52, "bob@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Mike", 18, "mike@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Katy", 34, "katy@mail.ru"));
    }*/
    public List<Person> index(){//все люди
        //return people;
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();//объект, содержащий запросы к бд
            String SQL = "SELECT * FROM person ORDER BY id_person";
            ResultSet resultSet = statement.executeQuery(SQL);//выполнение запроса (Query не изменяет данные, а получает)
            while (resultSet.next()){//добавление в список
                Person person = new Person();
                person.setId(resultSet.getInt("id_person"));
                person.setName(resultSet.getString("name_person"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
                /*people.add(new Person(resultSet.getInt("id_person"), resultSet.getString("name_person"),
                        resultSet.getInt("age"), resultSet.getString("email")));*/
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }
    public Person show(int id){//чел по id
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
        Person person = null;
        try {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM person WHERE id_person=?");//запрос
            prst.setInt(1, id);//подставить id в ?
            ResultSet resultSet = prst.executeQuery();//результат запроса
            resultSet.next();//полученная строка из бд
            person = new Person();
            person.setId(resultSet.getInt("id_person"));
            person.setName(resultSet.getString("name_person"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public void save(Person person){
        //person.setId(++PEOPLE_COUNT);
       // people.add(person);

        try {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO Person (name_person, age, email) VALUES (?, ?, ?)");
            prst.setString(1, person.getName());
            prst.setInt(2, person.getAge());
            prst.setString(3, person.getEmail());
            prst.executeUpdate();
        } catch (SQLException e) {throw new RuntimeException(e);}

    }
    public void update(int id, Person person){
        /*Person personToBeUpdated = show(id);

        personToBeUpdated.setName(person.getName());
        personToBeUpdated.setAge(person.getAge());
        personToBeUpdated.setEmail(person.getEmail());*/
        try {
            PreparedStatement prst = connection.prepareStatement("UPDATE person SET name_person=?, age=?, email=? WHERE id_person=?");
            prst.setString(1, person.getName());
            prst.setInt(2, person.getAge());
            prst.setString(3, person.getEmail());
            prst.setInt(4, id);
            prst.executeUpdate();
        } catch (SQLException e) {throw new RuntimeException(e);}

    }
    public void delete(int id){
       // people.removeIf(p -> p.getId() == id);
        try {
            PreparedStatement prst = connection.prepareStatement("DELETE FROM Person WHERE id_person=?");
            prst.setInt(1, id);
            prst.executeUpdate();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }
}
