package com.blog.app.services;

import com.blog.app.services.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadImage() throws IOException {
        // Arrange
        String path = "testPath";
        String originalFileName = "testImage.png";
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        // Act
        String fileName = fileService.uploadImage(path, file);

        // Debug Output
        System.out.println("Generated File Name: " + fileName);

        // Assert
        assertNotNull(fileName, "File name should not be null");
        // Check if fileName ends with ".png"
        assertTrue(fileName.endsWith(".png"), "File name should end with '.png'");
        // Check if fileName has a UUID prefix
        assertDoesNotThrow(() -> UUID.fromString(fileName.substring(0, fileName.lastIndexOf('.'))), "File name should start with a valid UUID");

        // Clean up
        File uploadedFile = new File(path + File.separator + fileName);
        if (uploadedFile.exists()) {
            uploadedFile.delete();
        }
    }

    @Test
    public void testGetResource() throws IOException {
        // Arrange
        String path = "src/test/resources";
        String fileName = "testImage.png";

        // Create a temporary file for testing
        File tempFile = new File(path + File.separator + fileName);
        tempFile.getParentFile().mkdirs();
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write("test content".getBytes());
        }

        // Act
        InputStream result = fileService.getResource(path, fileName);

        // Assert
        assertNotNull(result);
        assertEquals("test content", new String(result.readAllBytes()));

        // Clean up
        tempFile.delete();
    }
}