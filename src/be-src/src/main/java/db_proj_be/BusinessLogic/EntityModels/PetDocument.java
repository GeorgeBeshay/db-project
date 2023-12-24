package db_proj_be.BusinessLogic.EntityModels;

public class PetDocument {
    private int id;
    private int petId;
    private String documentType;
    private byte[] document;

    public PetDocument() {}
    public PetDocument(int petId, String documentType, byte[] document) {
        this.petId = petId;
        this.documentType = documentType;
        this.document = document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }
}
