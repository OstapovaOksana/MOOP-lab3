package db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class DbNews implements Serializable{
	public int id;
	public String name;
	public String publishingHouse;
	public int categoryId;
	
	public DbNews(int id, String name, String publishingHouse, int categoryId) {
		this.id = id;
		this.name = name;
		this.publishingHouse = publishingHouse;
		this.categoryId = categoryId;
	}
	
	public DbNews(DataInputStream in) throws Exception {
		id = in.readInt();
		name = in.readUTF();
		publishingHouse = in.readUTF();
		categoryId = in.readInt();
	}
	
	public void serialize(DataOutputStream out) throws Exception {
        out.writeInt(id);
        out.writeUTF(name);
        out.writeUTF(publishingHouse);
        out.writeInt(categoryId);
    }
	
	@Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publishingHouse='" + publishingHouse + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
