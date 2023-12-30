package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.PetDocumentDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PetDocumentService {

    private final JdbcTemplate jdbcTemplate;
    private final PetDocumentDAO petDocumentDAO;

    public PetDocumentService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.petDocumentDAO = new PetDocumentDAO(jdbcTemplate);
    }

    public int uploadFilesForPet(int petId, MultipartFile[] files) {
        for (MultipartFile file : files) {
            PetDocument petDocument = new PetDocument();
            petDocument.setPetId(petId);
            petDocument.setDocumentType(file.getContentType());
            petDocument.setName(file.getOriginalFilename());
            try {
                petDocument.setDocument(file.getBytes());
                return petDocumentDAO.create(petDocument);
            }

            catch (Exception e) {
                Logger.logMsgFrom(this.getClass().getName(), "A document is failed to be created" + e.getMessage(), 1);
                return -1;
            }
        }

        return -1;
    }

    public PetDocument downloadFile(int documentId) {
        PetDocument petDocument = petDocumentDAO.findById(documentId);

        if (petDocument == null || petDocument.getDocument() == null || petDocument.getDocument().length == 0) {
            Logger.logMsgFrom(this.getClass().getName(), "A document is failed to be downloaded", 1);
            return null;
        }

        return petDocument;
    }

}
