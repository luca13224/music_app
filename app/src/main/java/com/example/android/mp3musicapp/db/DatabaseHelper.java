package com.example.android.mp3musicapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.mp3musicapp.Model.BaiHat;
import com.example.android.mp3musicapp.Model.ChuDe;
import com.example.android.mp3musicapp.Model.PlayList;
import com.example.android.mp3musicapp.Model.TheLoai;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MusicApp.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)");

        // Tạo bảng Songs (loại bỏ albumId)
        db.execSQL("CREATE TABLE Songs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenBaiHat TEXT, " +
                "hinhBaiHat TEXT, " +
                "caSi TEXT, " +
                "linkBaiHat TEXT, " +
                "playListId INTEGER, " +
                "chuDeId INTEGER, " +
                "theLoaiId INTEGER, " +
                "luotThich INTEGER DEFAULT 0, " +
                "FOREIGN KEY (playListId) REFERENCES PlayLists(id), " +
                "FOREIGN KEY (chuDeId) REFERENCES ChuDe(id), " +
                "FOREIGN KEY (theLoaiId) REFERENCES TheLoai(id))");

        // Tạo bảng PlayLists
        db.execSQL("CREATE TABLE PlayLists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT, " +
                "hinhNen TEXT)");

        // Tạo bảng ChuDe
        db.execSQL("CREATE TABLE ChuDe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenChuDe TEXT, " +
                "hinhChuDe TEXT)");

        // Tạo bảng TheLoai
        db.execSQL("CREATE TABLE TheLoai (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenTheLoai TEXT, " +
                "hinhTheLoai TEXT)");

        // Thêm dữ liệu mẫu
        db.execSQL("INSERT INTO Users (username, email, password) VALUES " +
                "('user1', 'user1@example.com', '123456'), " +
                "('user2', 'user2@example.com', '654321')");

        db.execSQL("INSERT INTO PlayLists (ten, hinhNen) VALUES " +
                "('Playlist 1', 'https://example.com/playlist1.jpg'), " +
                "('Playlist 2', 'https://example.com/playlist2.jpg')");

        db.execSQL("INSERT INTO ChuDe (tenChuDe, hinhChuDe) VALUES " +
                "('Chủ Đề 1', 'https://example.com/chude1.jpg'), " +
                "('Chủ Đề 2', 'https://example.com/chude2.jpg')");

        db.execSQL("INSERT INTO TheLoai (tenTheLoai, hinhTheLoai) VALUES " +
                "('Thể Loại 1', 'https://example.com/theloai1.jpg'), " +
                "('Thể Loại 2', 'https://example.com/theloai2.jpg')");

        db.execSQL("INSERT INTO Songs (tenBaiHat, hinhBaiHat, caSi, linkBaiHat, playListId, chuDeId, theLoaiId, luotThich) VALUES " +
                "('Hay Trao Cho Anh', 'drawable/hay_trao_cho_anh_.jpg', 'Sơn Tùng M-TP', 'raw/haytraochoanh.mp3', 1, 1, 1, 5), " +
                "('Tái Sinh', 'drawable/tai_sinh.jpg', 'Tùng Dương', 'raw/taisinh.mp3', 2, 2, 2, 3), " +
                "('Bài Hát 3', 'https://example.com/song3.jpg', 'Ca Sĩ 1', 'https://example.com/song3.mp3', 1, 1, 2, 2), " +
                "('Bài Hát 4', 'https://example.com/song4.jpg', 'Ca Sĩ 3', 'https://example.com/song4.mp3', null, 2, 1, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Songs");
        db.execSQL("DROP TABLE IF EXISTS PlayLists");
        db.execSQL("DROP TABLE IF EXISTS ChuDe");
        db.execSQL("DROP TABLE IF EXISTS TheLoai");
        onCreate(db);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void addUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Users (email, username, password) VALUES (?, ?, ?)", new String[]{email, username, password});
    }

    public ArrayList<PlayList> getAllPlayLists() {
        ArrayList<PlayList> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PlayLists", null);
        if (cursor.moveToFirst()) {
            do {
                PlayList playList = new PlayList();
                playList.setIdPlayList(cursor.getInt(0));
                playList.setTen(cursor.getString(1));
                playList.setHinhNen(cursor.getString(2));
                list.add(playList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<ChuDe> getAllChuDe() {
        ArrayList<ChuDe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ChuDe", null);
        if (cursor.moveToFirst()) {
            do {
                ChuDe chuDe = new ChuDe();
                chuDe.setIdChuDe(cursor.getInt(0));
                chuDe.setTenChuDe(cursor.getString(1));
                chuDe.setHinhChuDe(cursor.getString(2));
                list.add(chuDe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<TheLoai> getTheLoaiByChuDe(int idChuDe) {
        ArrayList<TheLoai> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TheLoai", null); // Giả định không liên kết ChuDe-TheLoai
        if (cursor.moveToFirst()) {
            do {
                TheLoai theLoai = new TheLoai();
                theLoai.setIdTheLoai(cursor.getInt(0));
                theLoai.setTenTheLoai(cursor.getString(1));
                theLoai.setHinhTheLoai(cursor.getString(2));
                list.add(theLoai);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<BaiHat> getSongsByPlayList(int playListId) {
        ArrayList<BaiHat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Songs WHERE playListId = ?", new String[]{String.valueOf(playListId)});
        if (cursor.moveToFirst()) {
            do {
                BaiHat baiHat = new BaiHat();
                baiHat.setIdBaiHat(cursor.getInt(0));
                baiHat.setTenBaiHat(cursor.getString(1));
                baiHat.setHinhBaiHat(cursor.getString(2));
                baiHat.setCaSi(cursor.getString(3));
                baiHat.setLinkBaiHat(cursor.getString(4));
                baiHat.setIsLiked(cursor.getInt(8) > 0); // Đã thêm setIsLiked
                list.add(baiHat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<BaiHat> getSongsByTheLoai(int theLoaiId) {
        ArrayList<BaiHat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Songs WHERE theLoaiId = ?", new String[]{String.valueOf(theLoaiId)});
        if (cursor.moveToFirst()) {
            do {
                BaiHat baiHat = new BaiHat();
                baiHat.setIdBaiHat(cursor.getInt(0));
                baiHat.setTenBaiHat(cursor.getString(1));
                baiHat.setHinhBaiHat(cursor.getString(2));
                baiHat.setCaSi(cursor.getString(3));
                baiHat.setLinkBaiHat(cursor.getString(4));
                baiHat.setIsLiked(cursor.getInt(8) > 0); // Đã thêm setIsLiked
                list.add(baiHat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<BaiHat> searchSongs(String query) {
        ArrayList<BaiHat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Songs WHERE tenBaiHat LIKE ? OR caSi LIKE ?", new String[]{"%" + query + "%", "%" + query + "%"});
        if (cursor.moveToFirst()) {
            do {
                BaiHat baiHat = new BaiHat();
                baiHat.setIdBaiHat(cursor.getInt(0));
                baiHat.setTenBaiHat(cursor.getString(1));
                baiHat.setHinhBaiHat(cursor.getString(2));
                baiHat.setCaSi(cursor.getString(3));
                baiHat.setLinkBaiHat(cursor.getString(4));
                baiHat.setIsLiked(cursor.getInt(8) > 0); // Đã thêm setIsLiked
                list.add(baiHat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<BaiHat> getLikedSongs() {
        ArrayList<BaiHat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Songs WHERE luotThich > 0", null);
        if (cursor.moveToFirst()) {
            do {
                BaiHat baiHat = new BaiHat();
                baiHat.setIdBaiHat(cursor.getInt(0));
                baiHat.setTenBaiHat(cursor.getString(1));
                baiHat.setHinhBaiHat(cursor.getString(2));
                baiHat.setCaSi(cursor.getString(3));
                baiHat.setLinkBaiHat(cursor.getString(4));
                baiHat.setIsLiked(cursor.getInt(8) > 0); // Đã thêm setIsLiked
                list.add(baiHat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}