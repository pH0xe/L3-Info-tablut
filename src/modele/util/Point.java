package modele.util;

import java.util.Objects;

public class Point {
    private int l;
    private int c;


    public Point(int l, int c){
        this.l = l;
        this.c = c;
    }

    public int getL(){
        return l;
    }

    public int getC(){
        return c;
    }

    public void setL(int l){
        this.l = l;
    }

    public void setC(int c){
        this.c = c ;
    }

    @Override
    public String toString() {
        return "{" +
                "l=" + l +
                ", c=" + c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return l == point.l && c == point.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, c);
    }
}