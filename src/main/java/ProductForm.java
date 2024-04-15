import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ProductForm extends JFrame {
    private JTextField nameField, ageField, cityField, idField;

    public ProductForm() {
        setTitle("MongoDB CRUD Operations");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("City:"));
        cityField = new JTextField();
        panel.add(cityField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        panel.add(addButton);



        JButton readButton = new JButton("Read");
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                read();
            }
        });
        panel.add(readButton);
//        add(panel);
//        setVisible(true);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update();
                }
            });
            panel.add(updateButton);

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delete();
                }
            });
            panel.add(deleteButton);

        add(panel);
        setVisible(true);

    }



    private void add() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test2");
            MongoCollection<Document> collection = database.getCollection("persons");

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String city = cityField.getText();

            Document document = new Document()
                    .append("_id", id)
                    .append("name", name)
                    .append("age", age)
                    .append("city", city);

            collection.insertOne(document);
            JOptionPane.showMessageDialog(this, "Person added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void read() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test2");
            MongoCollection<Document> collection = database.getCollection("persons");

            int id = Integer.parseInt(idField.getText());
            Document query = new Document("_id", id);
            Document result = collection.find(query).first();

            if (result != null) {
                nameField.setText(result.getString("name"));
                ageField.setText(String.valueOf(result.getInteger("age")));
                cityField.setText(result.getString("city"));
            } else {
                JOptionPane.showMessageDialog(this, "Person not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void update() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test2");
            MongoCollection<Document> collection = database.getCollection("persons");

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String city = cityField.getText();

            Document query = new Document("_id", id);
            Document update = new Document("$set", new Document("name", name).append("age", age).append("city", city));

            collection.updateOne(query, update);
            JOptionPane.showMessageDialog(this, "Person updated successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void delete() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test2");
            MongoCollection<Document> collection = database.getCollection("persons");

            int id = Integer.parseInt(idField.getText());
            Document query = new Document("_id", id);

            collection.deleteOne(query);
            JOptionPane.showMessageDialog(this, "Person deleted successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        cityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductForm();
            }
        });
    }
}