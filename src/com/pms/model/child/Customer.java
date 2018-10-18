/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pms.model.child;


import com.pms.manifest.Symbol;
import com.pms.model.superb.SuperModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author RISITH-PC
 */

public class Customer extends SuperModel implements Comparable<Customer>{
    private String date;
    private String time;
    private String id;
    private String fName;
    private String lName;
    private String street;
    private String city;
    private String district;
    private String dob;
    private String nicNo;
    private String licNo;
    private String teleNo;
    private String whatsappNo;
    private String viberNo;
    private String email;
    private String issueDate;
    private String expireDate;
    private String points;

    public Customer() {}

    public Customer(String date, String time, String id, String fName, String lName, String street, String city, String district, String dob, String nicNo, String licNo, String teleNo, String whatsappNo, String viberNo, String email, String issueDate, String expireDate, String points) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.street = street;
        this.city = city;
        this.district = district;
        this.dob = dob;
        this.nicNo = nicNo;
        this.licNo = licNo;
        this.teleNo = teleNo;
        this.whatsappNo = whatsappNo;
        this.viberNo = viberNo;
        this.email = email;
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    public String getTeleNo() {
        return teleNo;
    }

    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    public String getWhatsappNo() {
        return whatsappNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    public String getViberNo() {
        return viberNo;
    }

    public void setViberNo(String viberNo) {
        this.viberNo = viberNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    
    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
  
    
    //---------------------------- Calculatons -------------------------------------//
   
    public String getFullName() {
        return fName + " " + lName;
    }
    
    public String getIdFullName() {
        return id+" - "+fName + " " + lName;
    }
    
    public String getAddress() {
        return street + ",\n" + city+ ",\n" + district;
    }
    
    public String getContacts() {
        return "T: " + teleNo + "\nW: " + whatsappNo + "\nV: " + viberNo + "\nE: " + email;
    }
    
    public String getIdentity() {
        return "NIC: " + nicNo + "\nLIC: " + licNo;
    }
     
    public int getAge() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date firstDate = sdf.parse(this.dob);
        Date secondDate = sdf.parse(sdf.format(new Date()));

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)/365;
    }
    
    public String getPeriod() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date firstDate = sdf.parse(sdf.format(new Date()));
        Date secondDate = sdf.parse(this.expireDate);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        int days = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return "Issue : " + issueDate + "\nExpire: " + expireDate + "\nLeft  : "+ days +" Days";
    }
    
    //---------------------------- Override Methods -------------------------------------//
    
    @Override
    public String toString() {
        return  getDate()       + Symbol.SPLIT +
                getTime()       + Symbol.SPLIT +
                getId()         + Symbol.SPLIT +
                getFName()      + Symbol.SPLIT +
                getLName()      + Symbol.SPLIT +
                getStreet()     + Symbol.SPLIT +
                getCity()       + Symbol.SPLIT +
                getDistrict()   + Symbol.SPLIT +
                getDob()        + Symbol.SPLIT +
                getNicNo()      + Symbol.SPLIT +
                getLicNo()      + Symbol.SPLIT +
                getTeleNo()     + Symbol.SPLIT +
                getWhatsappNo() + Symbol.SPLIT +
                getViberNo()    + Symbol.SPLIT +
                getEmail()      + Symbol.SPLIT +
                getIssueDate()  + Symbol.SPLIT +
                getExpireDate() + Symbol.SPLIT +
                getPoints();
    }      
    
    
    @Override
    public int compareTo(Customer dto) {
        boolean logic = Integer.parseInt(dto.getId()) > Integer.parseInt(this.getId());
        return  logic ? -1 : !logic ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer) {
            return Integer.parseInt(((Customer)obj).getId()) == Integer.parseInt(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {        
        int hash = Objects.hashCode(String.format("%05d", this.getId()));
        return hash;
    } 
}
