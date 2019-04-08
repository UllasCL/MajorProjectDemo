package com.ullas.majorproject;

public class Leader {
    private String uid;
    private String complaintscount;

    public Leader(String uid, String complaintscount) {
        this.uid = uid;
        this.complaintscount = complaintscount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComplaintscount() {
        return complaintscount;
    }

    public void setComplaintscount(String complaintscount) {
        this.complaintscount = complaintscount;
    }
}
