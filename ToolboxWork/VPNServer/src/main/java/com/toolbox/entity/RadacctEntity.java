package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class RadacctEntity {
    private long   radacctid;
    private String acctsessionid;
    private String acctuniqueid;
    private String username;
    private String groupname;
    private String realm;
    private String nasipaddress;
    private String nasportid;
    private String nasporttype;
    private Date   acctstarttime;
    private Date   acctupdatetime;
    private Date   acctstoptime;
    private int    acctinterval;
    private int    acctsessiontime;
    private String acctauthentic;
    private String connectinfo_start;
    private String connectinfo_stop;
    private long   acctinputoctets;
    private long   acctoutputoctets;
    private String calledstationid;
    private String callingstationid;
    private String acctterminatecause;
    private String servicetype;
    private String framedprotocol;
    private String framedipaddress;

    public long getRadacctid() {
        return radacctid;
    }

    public void setRadacctid(long radacctid) {
        this.radacctid = radacctid;
    }

    public String getAcctsessionid() {
        return acctsessionid;
    }

    public void setAcctsessionid(String acctsessionid) {
        this.acctsessionid = acctsessionid;
    }

    public String getAcctuniqueid() {
        return acctuniqueid;
    }

    public void setAcctuniqueid(String acctuniqueid) {
        this.acctuniqueid = acctuniqueid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getNasipaddress() {
        return nasipaddress;
    }

    public void setNasipaddress(String nasipaddress) {
        this.nasipaddress = nasipaddress;
    }

    public String getNasportid() {
        return nasportid;
    }

    public void setNasportid(String nasportid) {
        this.nasportid = nasportid;
    }

    public String getNasporttype() {
        return nasporttype;
    }

    public void setNasporttype(String nasporttype) {
        this.nasporttype = nasporttype;
    }

    public Date getAcctstarttime() {
        return acctstarttime;
    }

    public void setAcctstarttime(Date acctstarttime) {
        this.acctstarttime = acctstarttime;
    }

    public Date getAcctupdatetime() {
        return acctupdatetime;
    }

    public void setAcctupdatetime(Date acctupdatetime) {
        this.acctupdatetime = acctupdatetime;
    }

    public Date getAcctstoptime() {
        return acctstoptime;
    }

    public void setAcctstoptime(Date acctstoptime) {
        this.acctstoptime = acctstoptime;
    }

    public int getAcctinterval() {
        return acctinterval;
    }

    public void setAcctinterval(int acctinterval) {
        this.acctinterval = acctinterval;
    }

    public int getAcctsessiontime() {
        return acctsessiontime;
    }

    public void setAcctsessiontime(int acctsessiontime) {
        this.acctsessiontime = acctsessiontime;
    }

    public String getAcctauthentic() {
        return acctauthentic;
    }

    public void setAcctauthentic(String acctauthentic) {
        this.acctauthentic = acctauthentic;
    }

    public String getConnectinfo_start() {
        return connectinfo_start;
    }

    public void setConnectinfo_start(String connectinfo_start) {
        this.connectinfo_start = connectinfo_start;
    }

    public String getConnectinfo_stop() {
        return connectinfo_stop;
    }

    public void setConnectinfo_stop(String connectinfo_stop) {
        this.connectinfo_stop = connectinfo_stop;
    }

    public long getAcctinputoctets() {
        return acctinputoctets;
    }

    public void setAcctinputoctets(long acctinputoctets) {
        this.acctinputoctets = acctinputoctets;
    }

    public long getAcctoutputoctets() {
        return acctoutputoctets;
    }

    public void setAcctoutputoctets(long acctoutputoctets) {
        this.acctoutputoctets = acctoutputoctets;
    }

    public String getCalledstationid() {
        return calledstationid;
    }

    public void setCalledstationid(String calledstationid) {
        this.calledstationid = calledstationid;
    }

    public String getCallingstationid() {
        return callingstationid;
    }

    public void setCallingstationid(String callingstationid) {
        this.callingstationid = callingstationid;
    }

    public String getAcctterminatecause() {
        return acctterminatecause;
    }

    public void setAcctterminatecause(String acctterminatecause) {
        this.acctterminatecause = acctterminatecause;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getFramedprotocol() {
        return framedprotocol;
    }

    public void setFramedprotocol(String framedprotocol) {
        this.framedprotocol = framedprotocol;
    }

    public String getFramedipaddress() {
        return framedipaddress;
    }

    public void setFramedipaddress(String framedipaddress) {
        this.framedipaddress = framedipaddress;
    }

}
