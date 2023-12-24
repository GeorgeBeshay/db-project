package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import org.springframework.jdbc.core.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class PetDocumentDAO {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<PetDocument> rowMapper;

    public PetDocumentDAO (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(PetDocument.class);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
    }

    public BeanPropertyRowMapper<PetDocument> getRowMapper() {
        return rowMapper;
    }

    @Transactional
    public boolean saveDocument(PetDocument petDocument) {
        try {
            String sql = "INSERT INTO PET_DOCUMENT (pet_id, document_type, document) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, petDocument.getPetId(), petDocument.getDocumentType(), petDocument.getDocument());

            return rowsAffected > 0;
        }

        catch (Exception e) {
            System.out.println("The document of the pet can not be saved: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public List<PetDocument> findByPetId(int petId) {
        try {
            String sql = """
                SELECT *
                FROM PET_DOCUMENT
                WHERE pet_id = ?
                """;

            return jdbcTemplate.query(sql, rowMapper, petId);
        }

        catch (Exception e) {
            System.out.println("Can not get the documents of this pet: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<PetDocument> findByPetIdAndType(int petId, String type) {
        try {
            String sql = """
                SELECT *
                FROM PET_DOCUMENT
                WHERE pet_id = ? AND document_type = ?
                """;

            return jdbcTemplate.query(sql, rowMapper, petId, type);
        }

        catch (Exception e) {
            System.out.println("Can not get the documents of this pet: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean deleteDocument(PetDocument petDocument) {
        try {
            String sql = """
                DELETE FROM PET_DOCUMENT
                WHERE pet_id = ? AND document_type = ? AND document = ?
                """;

            int rowsAffected = jdbcTemplate.update(sql, petDocument.getPetId(), petDocument.getDocumentType(), petDocument.getDocument());
            return rowsAffected > 0;
        }

        catch (Exception e) {
            System.out.println("Can not delete the document of this pet: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateDocument(PetDocument petDocument) {
        try {
            String sql = """
                UPDATE PET_DOCUMENT
                SET pet_id = ?, document_type = ?, document = ?
                WHERE id = ?
                """;
            int rowsAffected = jdbcTemplate.update(sql, petDocument.getPetId(), petDocument.getDocumentType(), petDocument.getDocument(), petDocument.getId());

            return rowsAffected > 0;
        }

        catch (Exception e) {
            System.out.println("The document of the pet can not be updated: " + e.getMessage());
            return false;
        }
    }
}
