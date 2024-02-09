package db_proj_be.BusinessLogic.EntityModels;

import java.util.Arrays;
import java.util.Objects;

public class PetDocument implements Identifiable {
    private int id;
    private int petId;
    private String documentType;
    private String name;
    private byte[] document;

    public PetDocument() {}
    public PetDocument(int petId, String documentType, String name, byte[] document) {
        this.petId = petId;
        this.documentType = documentType;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetDocument that = (PetDocument) o;
        return id == that.id &&
                petId == that.petId &&
                Objects.equals(documentType, that.documentType) &&
                Objects.equals(name, that.name) &&
                Arrays.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, petId, documentType, name);
        result = 31 * result + Arrays.hashCode(document);
        return result;
    }

    @Override
    public String toString() {
        return "PetDocument{" +
                "id=" + id +
                ", petId=" + petId +
                ", documentType='" + documentType + '\'' +
                ", name='" + name + '\'' +
                ", document=" + Arrays.toString(document) +
                '}';
    }
}
