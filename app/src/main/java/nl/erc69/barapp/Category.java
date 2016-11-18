package nl.erc69.barapp;

import java.util.HashMap;

public class Category {

    private String name;
    private String id;
    private int position;

    public static final String CATEGORIES = "Categories";
    public static final String ITEMS = "Items";
    public static final String CATEGORY_POSITION = "categoryPosition";
    public static final String ITEM_POSITION = "itemPosition";

    public static final HashMap<Integer,Category> CATEGORIES_POS = new HashMap<Integer, Category>();
    public static final HashMap<String,Category> CATEGORIES_ID = new HashMap<String, Category>();

    public final HashMap<Integer,Item> ITEMS_POS = new HashMap<Integer, Item>();
    public final HashMap<String,Item> ITEMS_ID = new HashMap<String, Item>();

    public static void newCategory(Category category){
        CATEGORIES_POS.put(category.getPosition(),category);
        CATEGORIES_ID.put(category.getId(),category);
    }

    public static void clear(){
        CATEGORIES_POS.clear();
        CATEGORIES_POS.clear();
    }

    public void addItem(Item item){
        ITEMS_POS.put(item.getPosition(),item);
        ITEMS_ID.put(item.getId(),item);
    }

    public void removeItem(int mPosition){
        String mId = ITEMS_POS.get(mPosition).getId();
        ITEMS_POS.remove(mPosition);
        ITEMS_ID.remove(mId);
    }

    Category(){
        //Empty constructor for Firebase
    }

    Category(String name,String id,int position){
        this.name = name;
        this.id = id;
        this.position = position;
    }

    public String getName(){return name;}

    public String getId(){return id;}

    public int getPosition(){return position;}

    public void setName(String mName){name = mName;}

    public void setPosition(int mPosition){position=mPosition;}

}
