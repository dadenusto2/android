package com.example.list;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Student implements Parcelable {
    private String mFIO;
    private String mFacultet;
    private String mGroup;
    private ArrayList<Subject> mSubjects;

    public Student(String FIO, String facultet, String group) {
        mFIO = FIO;
        mFacultet = facultet;
        mGroup = group;
        mSubjects = new ArrayList<>();
    }

    protected Student(Parcel in) {
        mFIO = in.readString();
        mFacultet = in.readString();
        mGroup = in.readString();
        mSubjects = in.createTypedArrayList(Subject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFIO);
        dest.writeString(mFacultet);
        dest.writeString(mGroup);
        dest.writeTypedList(mSubjects);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getFIO() {
        return mFIO;
    }

    public void setFIO(String FIO) {
        mFIO = FIO;
    }

    public String getFacultet() {
        return mFacultet;
    }

    public void setFacultet(String facultet) {
        mFacultet = facultet;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    @Override
    public String toString() {
        return "Student{" +
                "mFIO='" + mFIO + '\'' +
                ", mFacultet='" + mFacultet + '\'' +
                ", mGroup='" + mGroup + '\'' +
                '}';
    }

    public ArrayList<Subject> getSubjects() {
        return mSubjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        mSubjects = subjects;
    }

    public void addSubject(Subject subject){
        mSubjects.add(subject);
    }

}
