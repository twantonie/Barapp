package nl.erc69.barapp;

import java.util.ArrayList;

public class Category {

    private String mName;
    public static final String CATEGORY_POSITION = "categoryPosition";
    public static final String ITEM_POSITION = "itemPosition";

    public final ArrayList<Item> ITEMS = new ArrayList<Item>();
    public static ArrayList<Category> CATEGORIES = new ArrayList<Category>();

    Category(String name){
        mName = name;
    }

    public String getName(){return mName;}

    public void addItem(String name,Double price){
        Item newItem = new Item(name,price);
        ITEMS.add(newItem);
    }

    public void setName(String name){mName = name;}

}
