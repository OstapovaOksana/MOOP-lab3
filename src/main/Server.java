package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.DbCategory;
import db.DbNews;
import db.DbNewsAgency;

public class Server {
	 private ServerSocket server = null;
	    private Socket sock = null;
	    private DataOutputStream out = null;
	    private DataInputStream in = null;
	    private DbNewsAgency services = new DbNewsAgency();

	    public void run(int port) throws Exception {
	        server = new ServerSocket(port);
	        services.connect("jdbc:sqlserver://DESKTOP-UM6VBB8\\MSSQLSERVER01;user=oksana;password=savonik-oksana;databaseName=MOOP-db;");

	        while (true) {
	            sock = server.accept();

	            in = new DataInputStream(sock.getInputStream());
	            out = new DataOutputStream(sock.getOutputStream());

	            while (processQuery()) ;
	        }
	    }

	    public void stop() throws Exception {
	        server.close();
	        services.disconnect();
	    }

	    private boolean processQuery() {
	        try {
	            int oper = in.readInt();
	            System.out.println(oper);

	            if (oper == OperationTypes.ADD_CATEGORY) {
	                int code = in.readInt();
	                String name = in.readUTF();
	                System.out.println(name);
	                out.writeBoolean(services.addCategory(name));
	            } else if (oper == OperationTypes.QUERY_CATEGORY) {
	                int code = in.readInt();
	                services.getCategory(code).serialize(out);
	            } else if (oper == OperationTypes.LIST_CATEGORIES) {
	                System.out.println("hello");
	                ArrayList<DbCategory> categories = services.getCategories();
	                out.writeInt(categories.size());
	                for (DbCategory airline : categories) {
	                    airline.serialize(out);
	                }
	            } else if (oper == OperationTypes.DELETE_CATEGORY) {
	                int code = in.readInt();
	                out.writeBoolean(services.deleteCategory(code));
	            } else if (oper == OperationTypes.ADD_NEWS) {
	                int code = in.readInt();
	                int categoryCode = in.readInt();
	                String name = in.readUTF();
	                String publishingHouse = in.readUTF();
	                out.writeBoolean(services.addNews(name, publishingHouse, categoryCode));
	            } else if (oper == OperationTypes.UPDATE_NEWS) {
	                int code = in.readInt();
	                int cnt = in.readInt();
	                Map<String, String> changes = new HashMap<>();
	                for (int i = 0; i < cnt; ++i) {
	                    changes.put(in.readUTF(), in.readUTF());
	                }
	                out.writeBoolean(services.updateNews(code, changes));
	            } else if (oper == OperationTypes.QUERY_NEWS) {
	                int code = in.readInt();
	                services.getNewsById(code).serialize(out);
	            } else if (oper == OperationTypes.LIST_NEWS) {
	                ArrayList<DbNews> news = services.getNews();
	                out.writeInt(news.size());
	                for (DbNews singleNew : news) {
	                	singleNew.serialize(out);
	                }
	            } else if (oper == OperationTypes.LIST_NEWS_BY_CATEGORY) {
	                int code = in.readInt();
	                ArrayList<DbNews> news = services.getNewsByCategory(code);
	                out.writeInt(news.size());
	                for (DbNews singleNew : news) {
	                	singleNew.serialize(out);
	                }
	            } else if (oper == OperationTypes.DELETE_NEWS) {
	                int code = in.readInt();
	                out.writeBoolean(services.deleteNews(code));
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        Server srv = new Server();
	        srv.run(12345);
	        srv.stop();
	    }

}
