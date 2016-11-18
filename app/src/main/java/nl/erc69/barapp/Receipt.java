package nl.erc69.barapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Receipt {

    public ArrayList<Line> LINES = new ArrayList<Line>();
    private final Calendar calendar;

    public static final String LINE_POSITION = "linePosition";
    public static final String ORDER_AMOUNT = "orderAmount";

    Receipt(){
        calendar = Calendar.getInstance();
    }

    public void addLine(int categoryPosition,int itemPosition,int orderAmount){
        LINES.add(new Line(categoryPosition,itemPosition,orderAmount));
    }

    public double getTotal(){
        double total=0;
        for(int i=0;i<LINES.size();i++){
            total+= LINES.get(i).getItem().getPrice()*LINES.get(i).getOrderAmount();
        }
        return total;
    }

    public String getParsedDate() {
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH)) + "-"
                + String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getParsedTime(){
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
    }

    public class Line {
        private int orderAmount;
        private final int categoryPosition;
        private final int itemPosition;


        Line(int mCategoryPosition,int mItemPosition,int mOrderAmount){
            categoryPosition = mCategoryPosition;
            itemPosition = mItemPosition;
            orderAmount = mOrderAmount;
        }

        public Item getItem(){return Category.CATEGORIES_POS.get(categoryPosition).ITEMS_POS.get(itemPosition);}

        public int getOrderAmount(){return orderAmount;}

        public void setOrderAmount(int mOrderAmount){orderAmount=mOrderAmount;}

        public int getCategoryPosition() {return categoryPosition;}

        public int getItemPosition() {return itemPosition;}


    }
}
