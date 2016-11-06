package miner_pojos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Andreas on 9/25/16.
 */
public class CommitInfo {
    private String hash;
    private String commiter;
    private int bugsInduced;
    private String message;
    private Calendar date;
    private List<FileInfo> filesInfo;

    public CommitInfo(String hash, String commiter, int bugsInduced, String message, Calendar date, List<FileInfo> filesInfo) {
        this.hash = hash;
        this.commiter = commiter;
        this.bugsInduced = bugsInduced;
        this.message = message;
        this.date = date;
        this.filesInfo = filesInfo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCommiter() {
        return commiter;
    }

    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }

    public int getBugsInduced() {
        return bugsInduced;
    }

    public void setBugsInduced(int bugsInduced) {
        this.bugsInduced = bugsInduced;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }


    public Calendar getTimestamp() {
        return date;
    }

    public void setTimestamp(Calendar timestamp) {
        this.date = timestamp;
    }

    public List<FileInfo> getFilesInfo() {
        return filesInfo;
    }

    public void setFilesInfo(List<FileInfo> filesInfo) {
        this.filesInfo = filesInfo;
    }
}
