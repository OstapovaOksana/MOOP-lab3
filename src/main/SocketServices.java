package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import db.DbCategory;
import db.DbNews;

public class SocketServices {
    private Socket sock = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public SocketServices(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
    }

    public void disconnect() throws IOException {
        sock.close();
    }

    public boolean addCategory(int code, String name) throws Exception {
        System.out.println("hehhedbcikbehdclsjb");
        out.writeInt(OperationTypes.ADD_CATEGORY);
        out.writeInt(code);
        out.writeUTF(name);
        return in.readBoolean();
    }

   
    public DbCategory getCategory(int code) throws Exception {
        out.writeInt(OperationTypes.QUERY_CATEGORY);
        out.writeInt(code);
        return new DbCategory(in);
    }

   
    public ArrayList<DbCategory> getCategories() throws Exception {
        out.writeInt(OperationTypes.LIST_CATEGORIES);
        ArrayList<DbCategory> categories = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            categories.add(new DbCategory(in));
        }
        return categories;
    }

   
    public boolean deleteCategory(int code) throws Exception {
        out.writeInt(OperationTypes.DELETE_CATEGORY);
        out.writeInt(code);
        return in.readBoolean();
    }

  
    public boolean addNews(int id, String name, String publishingHouse, int categoryId) throws Exception {
        out.writeInt(OperationTypes.ADD_NEWS);
        out.writeInt(id);
        out.writeInt(categoryId);
        out.writeUTF(name);
        out.writeUTF(publishingHouse);
        return in.readBoolean();
    }

    
    public boolean updateNews(int id, Map<String, String> changes) throws Exception {
        out.writeInt(OperationTypes.UPDATE_NEWS);
        out.writeInt(id);
        out.writeInt(changes.size());
        for (Map.Entry entry : changes.entrySet()) {
            out.writeUTF((String)entry.getKey());
            out.writeUTF((String)entry.getValue());
        }
        return in.readBoolean();
    }

  
    public DbNews getNewsById(int id) throws Exception {
        out.writeInt(OperationTypes.QUERY_NEWS);
        out.writeInt(id);
        return new DbNews(in);
    }

    public ArrayList<DbNews> getNews() throws Exception {
        out.writeInt(OperationTypes.LIST_NEWS);
        ArrayList<DbNews> news = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            news.add(new DbNews(in));
        }
        return news;
    }

    public ArrayList<DbNews> getNewsByCategory(int categoryId) throws Exception {
        out.writeInt(OperationTypes.LIST_NEWS_BY_CATEGORY);
        out.writeInt(categoryId);
        ArrayList<DbNews> news = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            news.add(new DbNews(in));
        }
        return news;
    }

    public boolean deleteNews(int id) throws Exception {
        out.writeInt(OperationTypes.DELETE_NEWS);
        out.writeInt(id);
        return in.readBoolean();
    }
}
