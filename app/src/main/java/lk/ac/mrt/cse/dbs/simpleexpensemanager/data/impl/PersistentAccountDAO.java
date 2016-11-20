package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Praveen on 11/20/2016.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        Cursor result = database.rawQuery("SELECT Account_no FROM Account",null);
        List<String> accounts = new ArrayList<String>();

        if(result.moveToFirst()) {
            do {
                accounts.add(result.getString(result.getColumnIndex("Account_no")));
            } while (result.moveToNext());
        }
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor result = database.rawQuery("SELECT * FROM Account",null);
        List<Account> accounts = new ArrayList<Account>();

        if(result.moveToFirst()) {
            do {
                Account account = new Account(result.getString(result.getColumnIndex("accountNo")),
                        result.getString(result.getColumnIndex("bankName")),
                        result.getString(result.getColumnIndex("accountHolderName")),
                        result.getDouble(result.getColumnIndex("balance")));
                accounts.add(account);
            } while (result.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = database.rawQuery("SELECT * FROM Account WHERE accountNo = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("accountNo")),
                        resultSet.getString(resultSet.getColumnIndex("bankName")),
                        resultSet.getString(resultSet.getColumnIndex("accountHolderName")),
                        resultSet.getDouble(resultSet.getColumnIndex("balance")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES ("+account.getAccountNo().toString()+","+account.getBankName().toString()+","+account.getAccountHolderName().toString()+","+Double.toString(account.getBalance());
        SQLiteStatement statement = database.compileStatement(sql);

        statement.executeInsert();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE accountNo ="+accountNo.toString();
        SQLiteStatement statement = database.compileStatement(sql);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amt = balance + ?";
        SQLiteStatement statement = database.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }
}
