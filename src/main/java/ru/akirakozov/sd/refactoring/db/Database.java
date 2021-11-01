package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.utils.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public void addProduct(String name, long price) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getProducts() {
        List<Item> items = new ArrayList<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                items.add(new Item(name, price));
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return items;
    }
}
