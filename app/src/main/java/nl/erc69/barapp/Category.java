package nl.erc69.barapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Category {

    private String name;
    private String id;
    private int position;

    public static final String CATEGORIES_FB = "Categories";
    public static final String ITEMS_FB = "Items";
    public static final String CATEGORY_POSITION = "categoryPosition";
    public static final String ITEM_POSITION = "itemPosition";
    public static final String POSITION = "position";

    public static final HashMap<Integer,Category> CATEGORIES_POS = new HashMap<Integer, Category>();
    public static final HashMap<String,Category> CATEGORIES_ID = new HashMap<String, Category>();

    public final HashMap<Integer,Item> ITEMS_POS = new HashMap<Integer, Item>();
    public final HashMap<String,Item> ITEMS_ID = new HashMap<String, Item>();

    public static void newCategory(Category category){
        CATEGORIES_POS.put(category.getPosition(),category);
        CATEGORIES_ID.put(category.getId(),category);
    }

    public static void removeCategory(int mPosition){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORIES_FB);
        Category category = CATEGORIES_POS.get(mPosition);
        int maxPosition = (CATEGORIES_POS.size()-1);

        if (mPosition < maxPosition){
            Category iterate;
            for (int i=mPosition;i<maxPosition;i++){
                iterate = CATEGORIES_POS.get(i+1);
                iterate.setPosition(i);
                mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                CATEGORIES_POS.remove(i);
                CATEGORIES_POS.put(i,iterate);
            }
        }

        CATEGORIES_POS.remove(maxPosition);
        CATEGORIES_ID.remove(category.getId());
        mDatabase.child(category.getId()).removeValue();
    }

    public static void clear(){
        CATEGORIES_POS.clear();
        CATEGORIES_POS.clear();
    }

    public static void setCategoryPosition(int newPosition,int oldPosition){
        if (oldPosition != newPosition) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORIES_FB);
            Category category = CATEGORIES_POS.get(oldPosition);

            Category iterate;
            if (oldPosition < newPosition){
                for (int i=oldPosition;i<newPosition;i++){
                    iterate = CATEGORIES_POS.get(i+1);
                    iterate.setPosition(i);
                    mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                    CATEGORIES_POS.remove(i);
                    CATEGORIES_POS.put(i,iterate);
                }
            } else{
                for (int i=oldPosition;i>newPosition;i--){
                    iterate = CATEGORIES_POS.get(i-1);
                    iterate.setPosition(i);
                    mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                    CATEGORIES_POS.remove(i);
                    CATEGORIES_POS.put(i,iterate);
                }
            }

            category.setPosition(newPosition);
            mDatabase.child(category.getId()).child(POSITION).setValue(newPosition);
            CATEGORIES_POS.remove(newPosition);
            CATEGORIES_POS.put(newPosition,category);

        }
    }

    public void addItem(Item item){
        ITEMS_POS.put(item.getPosition(),item);
        ITEMS_ID.put(item.getId(),item);
    }

    public void removeItem(int mPosition){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORIES_FB).child(id).child(ITEMS_FB);
        Item item = ITEMS_POS.get(mPosition);
        int maxPosition = (ITEMS_POS.size()-1);

        if (mPosition < maxPosition) {
            Item iterate;
            for (int i=mPosition;i<maxPosition;i++){
                iterate = ITEMS_POS.get(i+1);
                iterate.setPosition(i);
                mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                ITEMS_POS.remove(i);
                ITEMS_POS.put(i,iterate);
            }
        }

        ITEMS_POS.remove(maxPosition);
        ITEMS_ID.remove(item.getId());
        mDatabase.child(item.getId()).removeValue();

    }

    public void setItemPosition(int newPosition,int oldPosition){
        if (oldPosition != newPosition) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORIES_FB).child(id).child(ITEMS_FB);
            Item item = ITEMS_POS.get(oldPosition);

            Item iterate;
            if (oldPosition < newPosition){
                for (int i=oldPosition;i<newPosition;i++){
                    iterate = ITEMS_POS.get(i+1);
                    iterate.setPosition(i);
                    mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                    ITEMS_POS.remove(i);
                    ITEMS_POS.put(i,iterate);
                }
            } else{
                for (int i=oldPosition;i>newPosition;i--){
                    iterate = ITEMS_POS.get(i-1);
                    iterate.setPosition(i);
                    mDatabase.child(iterate.getId()).child(POSITION).setValue(i);
                    ITEMS_POS.remove(i);
                    ITEMS_POS.put(i,iterate);
                }
            }

            item.setPosition(newPosition);
            mDatabase.child(item.getId()).child(POSITION).setValue(newPosition);
            ITEMS_POS.remove(newPosition);
            ITEMS_POS.put(newPosition,item);

        }
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

    public void setPosition(int newPosition){position=newPosition;}

}
