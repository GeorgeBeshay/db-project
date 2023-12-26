package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class PetDocumentDAO extends DAO<PetDocument> {

    public PetDocumentDAO (JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, PetDocument.class, "PET_DOCUMENT");
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
            Logger.logMsgFrom(this.getClass().getName(), "Error in finding the document by pet Id: " + e.getMessage(), 1);
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
            Logger.logMsgFrom(this.getClass().getName(), "Error in finding the document by pet Id and type: " + e.getMessage(), 1);
            return null;
        }
    }

    @Transactional
    public List<PetDocument> findByPetIdAndName(int petId, String name) {
        try {
            String sql = """
                SELECT *
                FROM PET_DOCUMENT
                WHERE pet_id = ? AND name = ?
                """;

            return jdbcTemplate.query(sql, rowMapper, petId, name);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in finding the document by pet Id and name: " + e.getMessage(), 1);
            return null;
        }
    }

    @Transactional
    public boolean updateDocument(PetDocument petDocument) {
        try {
            String sql = """
                UPDATE PET_DOCUMENT
                SET pet_id = ?, document_type = ?, name = ?, document = ?
                WHERE id = ?
                """;
            int rowsAffected = jdbcTemplate.update(sql, petDocument.getPetId(), petDocument.getDocumentType(), petDocument.getName(), petDocument.getDocument(), petDocument.getId());

            return rowsAffected > 0;
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in updating the document of the pet: " + e.getMessage(), 1);
            return false;
        }
    }
}
