package miner_pojos;

/**
 * Created by Andreas on 9/25/16.
 */
public class Issue {
    private String id;
    private String typeId;
    private String typeName;
    private String message;

    public Issue(String id, String typeId, String typeName){
        this.id = id;
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
