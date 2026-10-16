package br.com.vistoria.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.http.Part;

public class UploadUtil {

    private UploadUtil() {
    }

    public static String salvar(Part part, String uploadDir) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }

        File pasta = new File(uploadDir);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String nomeOriginal = extrairNomeArquivo(part);
        String extensao = "";
        int ponto = nomeOriginal.lastIndexOf('.');
        if (ponto >= 0) {
            extensao = nomeOriginal.substring(ponto);
        }

        String nomeSalvo = UUID.randomUUID().toString() + extensao;
        Path destino = new File(pasta, nomeSalvo).toPath();

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        return nomeSalvo;
    }

    private static String extrairNomeArquivo(Part part) {
        String header = part.getHeader("content-disposition");
        for (String token : header.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).replace("\"", "");
            }
        }
        return "arquivo";
    }
}
