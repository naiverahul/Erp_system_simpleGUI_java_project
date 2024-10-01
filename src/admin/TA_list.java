package admin;
import student.TA;

import javax.swing.*;
import java.util.ArrayList;

public class TA_list {
    private ArrayList<TA> ta_list;
    public TA_list() {
        ta_list = new ArrayList<>();
    }
    public void add(TA t) {
        ta_list.add(t);
    }

    public ArrayList<TA> getTa_list() {
        return ta_list;
    }

    public void setTa_list(ArrayList<TA> ta_list) {
        this.ta_list = ta_list;
    }

    public void remove(TA t) {
        ta_list.remove(t);
    }
    public TA find_ta(TA t) {
        boolean found = false;
        for(TA i : ta_list) {
            if(i.equals(t)) {
                found = true;
                return i;
            }
        }
        if(!found) {
            return null;
        }
        return null;
    }
}
