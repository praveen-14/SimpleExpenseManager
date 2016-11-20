package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


/**
 * Created by Praveen on 11/20/2016.
 */
public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    public PersistentExpenseManager(Context context){

        this.context = context;
        setup();
    }
    @Override
    public void setup(){

        SQLiteDatabase mydatabase = context.openOrCreateDatabase("140577", context.MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE Account(accountNo VARCHAR,bankName VARCHAR,accountHolderName VARCHAR,balance REAL,primary key (Account_no));");

        mydatabase.execSQL("CREATE TABLE TransactionLog(TransactionID INTEGER,accountNo VARCHAR,expenseType INT,amount REAL,date DATE,primary key(Transaction_id),FOREIGN KEY (Account_no) REFERENCES Account(Account_no));");
        setAccountsDAO(new PersistentAccountDAO(mydatabase));

        setTransactionsDAO(new PersistentTransactionDAO(mydatabase));
    }
}

