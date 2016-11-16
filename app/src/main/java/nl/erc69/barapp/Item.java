package nl.erc69.barapp;



public class Item {
    private String mName;
    private double mPrice;
    private int mInStock;

    Item(String name,Double price){
        mName = name;
        mPrice = price;
    }

    public String getName(){return mName;}

    public void setName(String name){mName = name;}

    public Double getPrice(){return mPrice;}

    public void setPrice(double price){mPrice = price;}

    public boolean soldItem(int amount){
        if(amount>mInStock) {
            mInStock = 0;
            return false;
        }
        mInStock = mInStock-amount;
        return true;
    }

    public int getInStock(){return mInStock;}
}
