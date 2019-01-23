package com.ullas.majorproject;

public class Complaint
{
    String userID,complaintID,complaintAddress;
    public Complaint(){}

    public Complaint(String UserID,String ComplaintID,String ComplaintAddress)
    {
        this.userID=UserID;
        this.complaintID=ComplaintID;
        this.complaintAddress=ComplaintAddress;
    }

    public String getUserID()
    {
        return userID;
    }

    public String getComplaintID()
    {
        return complaintID;
    }

    public String getAddress()
    {
        return complaintAddress;
    }
}
