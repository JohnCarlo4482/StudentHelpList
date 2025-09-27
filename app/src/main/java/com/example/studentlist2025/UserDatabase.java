package com.example.studentlist2025;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String TAG = "UserDatabase";
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";

    // Singleton instance
    private static UserDatabase instance;

    // Private constructor to prevent direct instantiation
    private UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Get the singleton instance
    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UserDatabase(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL)";

        try {
            db.execSQL(createTable);
            Log.d(TAG, "Database table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add a new user to the database
     */
    public boolean addUser(String username, String password, String email) {
        if (username == null || password == null || email == null) {
            Log.e(TAG, "Cannot add user: null values provided");
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD, password); // In production, hash this password
            values.put(COLUMN_EMAIL, email);

            long result = db.insert(TABLE_NAME, null, values);
            boolean success = result != -1;

            if (success) {
                Log.d(TAG, "User added successfully: " + username);
            } else {
                Log.e(TAG, "Failed to add user: " + username);
            }

            return success;
        } catch (Exception e) {
            Log.e(TAG, "Error adding user: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Check if user credentials are valid
     */
    public boolean checkUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
            String[] selectionArgs = {username, password};

            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            boolean userExists = cursor.getCount() > 0;

            Log.d(TAG, "User authentication for " + username + ": " + userExists);
            return userExists;
        } catch (Exception e) {
            Log.e(TAG, "Error checking user: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Check if username already exists
     */
    public boolean userExists(String username) {
        if (username == null) {
            return false;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_USERNAME + "=?";
            String[] selectionArgs = {username};

            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking if user exists: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Check if email already exists
     */
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_EMAIL + "=?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking if email exists: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Get user details by username
     */
    public User getUser(String username) {
        if (username == null) {
            return null;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL};
            String selection = COLUMN_USERNAME + "=?";
            String[] selectionArgs = {username};

            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                return new User(id, username, email);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * Delete a user from the database
     */
    public boolean deleteUser(String username) {
        if (username == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String whereClause = COLUMN_USERNAME + "=?";
            String[] whereArgs = {username};

            int deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs);
            boolean success = deletedRows > 0;

            Log.d(TAG, "User deletion for " + username + ": " + success);
            return success;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting user: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Inner class to represent a User
     */
    public static class User {
        private int id;
        private String username;
        private String email;

        public User(int id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        // Getters
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }

        @Override
        public String toString() {
            return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
        }
    }
}