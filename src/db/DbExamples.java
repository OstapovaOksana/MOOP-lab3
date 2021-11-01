package db;

import java.util.HashMap;

import main.SocketServices;

public class DbExamples {
	private SocketServices services;
	
	public DbExamples(SocketServices services) {
        this.services = services;
    }
	
	public void runAll() throws Exception {
        example1();
        //example2();
        example3();
        example4();
    }
	
	public void example1() throws Exception {
        System.out.println("\nExample 1");
        
        services.addCategory(1, "New Category1");
        services.addCategory(2, "New Category2");
        System.out.println(services.getCategories());
//        services.deleteCategory(1);
//        System.out.println(services.getCategories());
    }

    public void example2() throws Exception {
        System.out.println("\nExample 2");

        System.out.println(services.getNews());
        services.addNews(1,"Name1", "PublishingHouse1", 1);
        services.addNews(2,"Name2", "PublishingHouse2", 2);
        System.out.println(services.getNews());
//        services.deleteNews(4);
//        System.out.println(services.getNews());
    }

    public void example3() throws Exception {
        System.out.println("\nExample 3");

        System.out.println(services.getNewsByCategory(1));
        System.out.println(services.getNewsByCategory(2));
    }

    public void example4() throws Exception {
        System.out.println("\nExample 4");

        System.out.println(services.getNewsByCategory(1));
        services.updateNews(1, new HashMap<String, String>() {{
            put("name", "Changed Name");
            put("publishingHouse", "Changed Publishing House");
        }});
        System.out.println(services.getNewsByCategory(1));
    }
}
