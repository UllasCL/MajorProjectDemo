package com.ullas.majorproject.DatabaseClasses;

public class Complaint {
    public String userID, complaintID, complaintAddress, resolved, agentno, publicORprivate, time, clearedtime, date;

    public Complaint() {
    }

    public Complaint(String userID, String complaintID, String complaintAddress, String resolved, String agentno, String publicORprivate, String time, String clearedtime, String date) {
        this.userID = userID;
        this.complaintID = complaintID;
        this.complaintAddress = complaintAddress;
        this.resolved = resolved;
        this.agentno = agentno;
        this.publicORprivate = publicORprivate;
        this.time = time;
        this.clearedtime = clearedtime;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public String getComplaintID() {
        return complaintID;
    }

    public String getClearedtime() {
        return clearedtime;
    }

    public void setClearedtime(String clearedtime) {
        this.clearedtime = clearedtime;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getAgentno() {
        return agentno;
    }

    public void setAgentno(String agentno) {
        this.agentno = agentno;
    }

    public String getPublicORprivate() {
        return publicORprivate;
    }

    public void setPublicORprivate(String publicORprivate) {
        this.publicORprivate = publicORprivate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return complaintAddress;
    }
}
