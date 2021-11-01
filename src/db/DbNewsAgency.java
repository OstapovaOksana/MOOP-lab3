package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class DbNewsAgency {
	private Connection con;
    private Statement stmt;
    
    public DbNewsAgency() {
    	con = null;
        stmt = null;
    }
    
    public void connect(String url) throws Exception {
        if (con != null) throw new Exception("Connection already exists");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        con = DriverManager.getConnection(url);
        stmt = con.createStatement();
    }
    
    public void disconnect() throws Exception {
        if (con == null) throw new Exception("There is no active connection");
        con.close();
        con = null;
        stmt = null;
    }
    
    public boolean addCategory(String name) throws Exception {
        String sql = "INSERT INTO Category (name)" +
                "VALUES ('" + name + "')";
        System.out.println(sql);
        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully added category");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add category");
            return false;
        }
    }
    
    public DbCategory getCategory(int id) throws Exception {
        String sql = "SELECT id, name FROM Category WHERE id=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int categoryId = rs.getInt("id");
                String name = rs.getString("name");
                return new DbCategory(categoryId, name);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occurred while requesting category with id " + id);
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public ArrayList<DbCategory> getCategories() throws Exception {
        ArrayList<DbCategory> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM Category";
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categories.add(new DbCategory(id, name));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occurred while requesting all categories");
            System.out.println(e.getMessage());
        }
        return categories;
    }
    
    public boolean updateCategory(int id, Map<String,String> changes) {
        StringBuilder assignments = new StringBuilder();
        for (Map.Entry<String, String> change : changes.entrySet()) {
            if (assignments.length() > 0)
                assignments.append(", ");
            assignments.append(change.getKey() + "='" + change.getValue() + "'");
        }

        String sql = "UPDATE Category" +
                " SET " + assignments.toString() + " WHERE id=" + id;
        System.out.println(sql);

        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully updated category with id " + id);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update category with id " + id);
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean deleteCategory(int id) throws Exception {
        String sql = "DELETE FROM Category WHERE id = " + id;
        try {
            int result = stmt.executeUpdate(sql);
            if (result > 0) {
                System.out.println("Category with id " + id + " successfully deleted!");
                return true;
            } else {
                System.out.println("Category with id " + id + " wasn't found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting category with id " + id);
            System.out.println(e.getMessage());
            return false;
        }
    }
    

    public boolean addNews(String name, String publishingHouse, int categoryId) {
		String sql = "INSERT INTO News (name, publishingHouse, categoryId)" +
				"VALUES ('" + name + "', '" + publishingHouse + "', '" + categoryId +  "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Successfully added news");
			return true;
		} catch (SQLException e) {
			System.out.println("Failed to add news");
			System.out.println(e.getMessage());
			return false;
		}
    }
    
    public boolean updateNews(int id, Map<String,String> changes) {
        StringBuilder assignments = new StringBuilder();
        for (Map.Entry<String,String> change : changes.entrySet()) {
            if (assignments.length() > 0)
                assignments.append(", ");
            assignments.append(change.getKey() + "='" + change.getValue() + "'");
        }

        String sql = "UPDATE News" +
                " SET " + assignments.toString() + " WHERE id=" + id;
        System.out.println(sql);

        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully updated news with id " + id);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update news with id " + id);
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public DbNews getNewsById(int id) {
        String sql = "SELECT * FROM News WHERE id=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int newsId = rs.getInt("id");
                String name = rs.getString("name");
                String publishingHouse = rs.getString("publishingHouse");
                int categoryId = rs.getInt("categoryId");

                return new DbNews(newsId, name, publishingHouse, categoryId);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting news with id " + id);
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public ArrayList<DbNews> getNews() {
        ArrayList<DbNews> news = new ArrayList<>();
        String sql = "SELECT * FROM News";
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String publishingHouse = rs.getString("publishingHouse");
                int categoryId = rs.getInt("categoryId");

                news.add(new DbNews(id, name, publishingHouse, categoryId));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting all news");
            System.out.println(e.getMessage());
        }
        return news;
    }
    
    public ArrayList<DbNews> getNewsByCategory(int categoryId) {
        ArrayList<DbNews> news = new ArrayList<>();
        String sql = "SELECT * FROM News WHERE categoryId=" + categoryId;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String publishingHouse = rs.getString("publishingHouse");
                int newsCategoryId = rs.getInt("categoryId");

                news.add(new DbNews(id,name, publishingHouse, newsCategoryId));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting news by category");
            System.out.println(e.getMessage());
        }
        return news;
    }
    
    public boolean deleteNews(int id) {
        String sql = "DELETE FROM News WHERE id = " + id;
        try {
            int result = stmt.executeUpdate(sql);
            if (result > 0) {
                System.out.println("News with id " + id + " successfully deleted!");
                return true;
            } else {
                System.out.println("News with id " + id + " wasn't found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("An error occured while deleting news with id " + id);
            System.out.println(e.getMessage());
            return false;
        }
    }    
}
