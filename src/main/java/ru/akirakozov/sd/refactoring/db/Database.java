package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.utils.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final String DATABASE_URL = "jdbc:sqlite:test.db";

    public void addProduct(String name, int price) {
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
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
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
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

    public Item getMaxItem() {
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            Item item = null;
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                item = new Item(name, price);
            }

            rs.close();
            stmt.close();

            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Item getMinItem() {
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            Item item = null;
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                item = new Item(name, price);
            }

            rs.close();
            stmt.close();

            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCount() {
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            stmt.close();

            return count;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getSum() {
        try (Connection c = DriverManager.getConnection(DATABASE_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
            long sum = 0;
            if (rs.next()) {
                sum = rs.getLong(1);
            }

            rs.close();
            stmt.close();
            return sum;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
