package com.example.chessappgroupd.domain.pgn;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/pgn")
@CrossOrigin(origins = "http://localhost:3000")
public class pgnController {

    private final pgnService pgnService;

    @PostMapping("/upload")
    public ResponseEntity<pgnFile> pgnUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam("username") String username) {
        try {
            return new ResponseEntity<>(pgnService.saveFile(file, username), HttpStatus.OK);
        }
        catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/download/{fileID}")
    public  ResponseEntity<byte[]> pgnDownload(@PathVariable("fileID") Long id) {
        pgnFile file = pgnService.getFile(id);
        return new ResponseEntity<>(file.getContent(), HttpStatus.OK);
    }

    @GetMapping("/download-gh/{gameID}")
    public  ResponseEntity<byte[]> pgnHistoryDownload(@PathVariable("gameID") Long id) {
        pgnFile file = pgnService.getHistoryFile(id);
        return new ResponseEntity<>(file.getContent(), HttpStatus.OK);
    }

    @GetMapping("/my-pgn")
    public ResponseEntity<List<pgnFile>> listPGN(@RequestParam("username") String username) {
        return new ResponseEntity<>(pgnService.getMyPGN(username),HttpStatus.OK);
    }


    @DeleteMapping("/delete/{fileId}")
    public  ResponseEntity<?> pgnDelete(@PathVariable("fileID") Long id) {
        pgnService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
