package com.example.finalproject;

class ClassItem {
    private final int classId;
    private final String className;
    private final String classLevel;

    public ClassItem(int classId, String className, String classLevel) {
        this.classId = classId;
        this.className = className;
        this.classLevel = classLevel;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassLevel() {
        return classLevel;
    }

    @Override
    public String toString() {
        return className + (classLevel.isEmpty() ? "" : " (" + classLevel + ")");
    }}
