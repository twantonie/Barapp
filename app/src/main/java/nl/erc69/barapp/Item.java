package nl.erc69.barapp;



public class Item {
    private String name;
    private String id;
    private int position;
    private double price;

    Item(){
        //Empty constructor for Firebase
    }

    Item(String mName,String mId,int mPosition,Double mPrice){
        name = mName;
        id = mId;
        position = mPosition;
        price = mPrice;
    }

    public String getName(){return name;}

    public void setName(String mName){
        name = mName;}

    public Double getPrice(){return price;}

    public void setPrice(double mprice){
        price = mprice;}

    public String getId(){
        return id;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position) {this.position = position;}


}
