package nl.erc69.barapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;

public class Receipt {

    public static Receipt currentOrderReceipt;

    private ArrayList<Line> LINES = new ArrayList<Line>();

    public static final String LINE_POSITION = "linePosition";
    public static final String ORDER_AMOUNT = "orderAmount";

    public static final String RECEIPT_FB = "Receipts";
    public static final String RECEIPT_CURRENT_FB = "Current";
    private static final String RECEIPT_PAID_FB = "Paid";
    private static final String TIME_FB = "Time";
    //private static final String DATE_FB = "Date";

    private static String FBId;
    private static DatabaseReference receiptDatabase;

    private final String date;
    private final String time;
    private String name = "";
    private int paymentType = -1; //0 is pin 1 is cash. -1 is not initialized

    public static void CurrentReceiptFireBase(DataSnapshot receipt){
        FBId = FirebaseInstanceId.getInstance().getId();

        if (receipt.child(FBId).exists()) {
            currentOrderReceipt = openReceiptFB(receipt.child(FBId));
            for (int x = 0; x < (receipt.child(FBId).getChildrenCount()-4); x++) {
                currentOrderReceipt.LINES.add(currentOrderReceipt.openLineFB(receipt.child(FBId).child(String.valueOf(x))));
            }
        }

        receiptDatabase = FirebaseDatabase.getInstance().getReference().child(RECEIPT_FB);
    }

    public static void NewCurrentReceipt(){
        currentOrderReceipt = new Receipt();
        currentOrderReceipt.addReceiptFB(receiptDatabase.child(RECEIPT_CURRENT_FB).child(FBId));
    }

    private Receipt(){
        Calendar calendar = Calendar.getInstance();
        date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH)) + "-"
                + String.valueOf(calendar.get(Calendar.YEAR));
        time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
    }

    private Receipt(String time, String date, String name, int paymentType){
        this.time = time;
        this.date = date;
        this.name = name;
        this.paymentType = paymentType;
    }

    public void addLine(String categoryId, String itemId, int orderAmount){
        Line line = new Line(categoryId, itemId, orderAmount);
        addLineFB(receiptDatabase.child(RECEIPT_CURRENT_FB).child(FBId).child(String.valueOf(LINES.size())), line);
        LINES.add(line);
    }

    public boolean removeLine(int linePosition){
        boolean lastLine = false;

        DatabaseReference database = receiptDatabase.child(RECEIPT_CURRENT_FB).child(FBId);
        database.removeValue();

        if (Receipt.currentOrderReceipt.LINES.size() > 1){
            Receipt.currentOrderReceipt.LINES.remove(linePosition);

            addReceiptToDatabase(database);
        } else{
            Receipt.currentOrderReceipt = null;
            lastLine = true;
        }
        return lastLine;
    }

    public Line getLine(int linePosition){
        return LINES.get(linePosition);
    }

    public int getLinesSize(){
        return LINES.size();
    }

    public double getTotal(){
        double total=0;
        for(int i=0;i<LINES.size();i++){
            total+= LINES.get(i).getItem().getPrice()*LINES.get(i).getOrderAmount();
        }
        return total;
    }

    public void CurrentReceiptPaid(int paymentType){
        this.paymentType = paymentType;

        receiptDatabase.child(RECEIPT_CURRENT_FB).child(FBId).removeValue();

        addReceiptToDatabase(receiptDatabase.child(RECEIPT_PAID_FB).child(date));

        currentOrderReceipt = null;
    }

    public String getDate() {
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getName(){
        return name;
    }

    public int getPaymentType(){
        return paymentType;
    }

    public void setName(String name){
        this.name = name;
    }

    private void addReceiptToDatabase(DatabaseReference database){
        addReceiptFB(database);
        for (int x = 0; x < LINES.size(); x++) {
            addLineFB(database.child(String.valueOf(x)), LINES.get(x));
        }
    }

    private void addReceiptFB(DatabaseReference database){
        database.child("Date").setValue(date);
        database.child("Time").setValue(time);
        database.child("Name").setValue(name);
        database.child("PaymentType").setValue(paymentType);
    }

    private void addLineFB(DatabaseReference database, Line line){
        database.child("ItemID").setValue(line.itemId);
        database.child("CategoryID").setValue(line.categoryId);
        database.child("OrderAmount").setValue(line.orderAmount);
    }

    private static Receipt openReceiptFB(DataSnapshot snapshot){
        return new Receipt(
                snapshot.child("Time").getValue(String.class),
                snapshot.child("Date").getValue(String.class),
                snapshot.child("Name").getValue(String.class),
                snapshot.child("PaymentType").getValue(Integer.class)
        );
    }

    private Line openLineFB(DataSnapshot snapshot){
        return new Line(
                snapshot.child("CategoryID").getValue(String.class),
                snapshot.child("ItemID").getValue(String.class),
                snapshot.child("OrderAmount").getValue(Integer.class)
        );
    }

    public class Line {
        private int orderAmount;
        private final String categoryId;
        private final String itemId;


        Line(String mCategoryId, String mItemId, int mOrderAmount){
            categoryId = mCategoryId;
            itemId = mItemId;
            orderAmount = mOrderAmount;
        }

        public Item getItem(){return Category.CATEGORIES_ID.get(categoryId).ITEMS_ID.get(itemId);}

        public int getOrderAmount(){return orderAmount;}

        public void setOrderAmount(int mOrderAmount){orderAmount=mOrderAmount;}

        public String getCategoryId() {return categoryId;}

        public String getItemId() {return itemId;}


    }
}
