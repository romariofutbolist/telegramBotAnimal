package skyPro.telegramBotAnimal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import skyPro.telegramBotAnimal.model.Avatar;
import skyPro.telegramBotAnimal.model.PetReport;
import skyPro.telegramBotAnimal.repository.AvatarRepository;
import skyPro.telegramBotAnimal.repository.ReportRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final static Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;
    private final ReportRepository reportRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarPetRepository, ReportRepository reportRepository) {
        this.avatarRepository = avatarPetRepository;
        this.reportRepository = reportRepository;
    }

    public void uploadAvatar(Long petReportId, MultipartFile avatarFile) throws IOException {
        logger.info("Upload avatar was invoked!");
        PetReport petReport = reportRepository.getById(petReportId);
        Path filePath = Path.of(avatarsDir, petReport + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            logger.info("Converting bytes...");
            bis.transferTo(bos);
        }
        logger.info("File has been uploaded!");

        Avatar avatar = findAvatar(petReportId);
        avatar.setPetReport(petReport);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateDataForDB(filePath));


        //avatarRepository.save(avatar);
        //logger.info("Avatar has been saved! id = {}, path = {}", avatar.getId(),filePath);
    }

    private byte[] generateDataForDB(Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }


    public Avatar findAvatar(Long petReportId) {
        return avatarRepository.findById(petReportId).orElse(null);
    }



    /*
    public void deleteAvatar(Long petReport) {
        avatarRepository.deleteAvatar(petReport);

    }

     */

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> getPage(int pageNumber, int pageSize) {
        return avatarRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }
}
