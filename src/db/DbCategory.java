package db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class DbCategory implements Serializable {
	public int id;
    public String name;

    public DbCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DbCategory(DataInputStream in) throws Exception {
        id = in.readInt();
        name = in.readUTF();
    }

    public void serialize(DataOutputStream out) throws Exception {
        out.writeInt(id);
        out.writeUTF(name);
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
