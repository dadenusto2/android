package com.example.list;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {
    String mName;
    Integer mMark;

    public Subject(String name, Integer mark) {
        mName = name;
        mMark = mark;
    }

    protected Subject(Parcel in) {
        mName = in.readString();
        if (in.readByte() == 0) {
            mMark = null;
        } else {
            mMark = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        if (mMark == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mMark);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public String toString() {
        return "Subject{" +
                "mName='" + mName + '\'' +
                ", mMark=" + mMark +
                '}';
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Integer getMark() {
        return mMark;
    }

    public void setMark(Integer mark) {
        mMark = mark;
    }
}
