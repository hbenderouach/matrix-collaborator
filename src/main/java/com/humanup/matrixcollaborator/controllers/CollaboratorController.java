package com.humanup.matrixcollaborator.controllers;

import com.humanup.matrixcollaborator.bs.CollaboratorBS;
import com.humanup.matrixcollaborator.exceptions.ProfileException;
import com.humanup.matrixcollaborator.vo.CollaboratorVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CollaboratorController {

    @Autowired
    private CollaboratorBS collaboratorBS;

    @Operation(summary = "Create Collaborator", description = " Create new collaborator by firstname, lastname ...", tags = { "collaborator" })
    @RequestMapping(value="/collaborator", method= RequestMethod.POST,consumes={ "application/json"})
    @ResponseBody
    public ResponseEntity createCollaborator(@RequestBody CollaboratorVO collaborator) throws ProfileException {
        Optional<Object> findCollaborator = Optional.ofNullable(collaboratorBS.findCollaboratorByMailAdresse(collaborator.getMailAdresse()));

        if(findCollaborator.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body("This User is Founded");
        }
        collaboratorBS.createCollaborator(collaborator);
        return ResponseEntity.status(HttpStatus.CREATED).body(collaborator);
    }

    @Operation(summary = "Find collaborator by email", description = "Collaborator search by %email% format", tags = { "collaborator" })
    @RequestMapping(value="/collaborator", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getCollaboratorInfo(@RequestParam(value="email", defaultValue="robot@sqli.com") String mailAdresse){
        Optional<CollaboratorVO> findCollaborator = Optional.ofNullable(collaboratorBS.findCollaboratorByMailAdresse(mailAdresse));
        if(findCollaborator.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(findCollaborator.get());
    }

    @Operation(summary = "Find all collaborators", description = "Find all collaborators", tags = { "collaborator" })
    @RequestMapping(value="/collaborator/all", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllCollaboratorInfo(){
        List<CollaboratorVO> findCollaborators = collaboratorBS.findListCollaborator();

        if(findCollaborators.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(findCollaborators);
    }

    @Operation(summary = "Find all collaborators by profile", description = "Find all collaborators by profile title", tags = { "collaborator" })
    @RequestMapping(value="/collaborator/all/profile", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getProfileCollaborators(@RequestParam(value="title", defaultValue="Spring Developer") String profileTitle){
        Optional<List<CollaboratorVO>> findProfile = Optional.ofNullable(collaboratorBS.findListProfilesByProfileTitle(profileTitle));
        if(findProfile.get().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Title not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(findProfile.get());
    }

}
