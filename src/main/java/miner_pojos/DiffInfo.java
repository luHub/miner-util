package miner_pojos;

/**
 * Created by mey on 9/26/2016.
 */
//"@@ -423,6  +423,9 @@"
public class DiffInfo {

    private Integer linesModifications;
    private Integer lineNumber;

    public DiffInfo(Integer linesModifications,Integer lineNumber){
        this.linesModifications=linesModifications;
        this.lineNumber=lineNumber;
    }

    public Integer getLinesModifications() {
        return linesModifications;
    }

    public void setLinesModifications(Integer linesModifications) {
        this.linesModifications = linesModifications;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}
