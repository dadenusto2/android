package com.example.list;

public class Student {
    private String mFIO;
    private String mFacultet;
    private String mGroup;

    public Student(String FIO, String facultet, String group) {
        mFIO = FIO;
        mFacultet = facultet;
        mGroup = group;
    }

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


}
