package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import db_proj_be.BusinessLogic.Services.PetDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pasms-server/pet-document-api/")
public class PetDocumentAPI {

    private final JdbcTemplate jdbcTemplate;
    private final PetDocumentService petDocumentService;

    @Autowired
    public PetDocumentAPI(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.petDocumentService = new PetDocumentService(jdbcTemplate);
    }

    @PostMapping("upload/{pet_id}")
    @ResponseBody
    public ResponseEntity<Integer> uploadFilesForPet(@PathVariable("pet_id") int petId,
                                                     @RequestParam("files") MultipartFile[] files) {
        int result = this.petDocumentService.uploadFilesForPet(petId, files);

        return new ResponseEntity<>(result, (result > 0) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("download/{documentId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") int documentId) {
        PetDocument petDocument = petDocumentService.downloadFile(documentId);

        if (petDocument == null) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayResource resource = new ByteArrayResource(petDocument.getDocument());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + petDocument.getName())
                .contentType(MediaType.parseMediaType(petDocument.getDocumentType()))
                .contentLength(petDocument.getDocument().length)
                .body(resource);
    }

}
