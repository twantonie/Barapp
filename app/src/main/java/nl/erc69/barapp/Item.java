package nl.erc69.barapp;



public class Item {
    private final String mName;
    private final double mPrice;
    private int mInStock;

    Item(String name,Double price){
        mName = name;
        mPrice = price;
    }

    public String getName(){return mName;}

    public Double getPrice(){return mPrice;}

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
